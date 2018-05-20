package mcjty.needtobreathe.blocks;

import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.tileentity.GenericEnergyStorageTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.needtobreathe.CommandHandler;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class PurifierGui extends GenericGuiContainer<CommonPurifierTileEntity> {
    public static final int PURIFIER_WIDTH = 180;
    public static final int PURIFIER_HEIGHT = 152;

    private EnergyBar energyBar;
    private EnergyBar coalBar;

    private static final ResourceLocation iconLocation = new ResourceLocation(NeedToBreathe.MODID, "textures/gui/purifier.png");
//    private static final ResourceLocation iconGuiElements = new ResourceLocation(NeedToBreathe.MODID, "textures/gui/guielements.png");

    public PurifierGui(CommonPurifierTileEntity tileEntity, GenericContainer container) {
        super(NeedToBreathe.instance, NTBMessages.INSTANCE, tileEntity, container, /*NeedToBreathe.GUI_MANUAL_MAIN*/ -1, "purifier");
        GenericEnergyStorageTileEntity.setCurrentRF(tileEntity.getEnergyStored());

        xSize = PURIFIER_WIDTH;
        ySize = PURIFIER_HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();

        int maxEnergyStored = tileEntity.getMaxEnergyStored();
        energyBar = new EnergyBar(mc, this).setVertical()
                .setMaxValue(maxEnergyStored)
                .setLayoutHint(new PositionalLayout.PositionalHint(10, 7, 8, 54))
                .setTooltips("Amount of energy left", "for purifying air")
                .setShowText(false);
        energyBar.setValue(GenericEnergyStorageTileEntity.getCurrentRF());

        coalBar = new EnergyBar(mc, this).setVertical().setMaxValue(tileEntity.getMaxCoalTicks())
                .setLayoutHint(new PositionalLayout.PositionalHint(20, 7, 8, 54))
                .setEnergyOnColor(0xffaaaaaa)
                .setEnergyOffColor(0xff444444)
                .setTooltips("Amount of coal left", "for purifying air")
                .setShowText(false);
        coalBar.setValue(tileEntity.getCoalticks());


        Panel toplevel = new Panel(mc, this).setBackground(iconLocation).setLayout(new PositionalLayout())
                .addChild(energyBar)
                .addChild(coalBar);
        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
        sendServerCommand(NeedToBreathe.MODID, CommandHandler.CMD_REQUESTINTEGERS,
                TypedMap.builder().put(CommandHandler.PARAM_POS, tileEntity.getPos()).build());
//        tileEntity.requestRfFromServer(NeedToBreathe.MODID);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();

        energyBar.setValue(tileEntity.getEnergyStored());
        coalBar.setMaxValue(tileEntity.getMaxCoalTicks());
        coalBar.setValue(tileEntity.getCoalticks());

        sendServerCommand(NeedToBreathe.MODID, CommandHandler.CMD_REQUESTINTEGERS,
                TypedMap.builder().put(CommandHandler.PARAM_POS, tileEntity.getPos()).build());
//        tileEntity.requestRfFromServer(NeedToBreathe.MODID);
    }
}
