package mcjty.needtobreathe.blocks;

import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.Widget;
import mcjty.lib.tileentity.GenericEnergyStorageTileEntity;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.network.NTBMessages;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class AirCompressorGui extends GenericGuiContainer<AirCompressorTileEntity> {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private EnergyBar energyBar;

    private static final ResourceLocation iconLocation = new ResourceLocation(NeedToBreathe.MODID, "textures/gui/air_compressor.png");
//    private static final ResourceLocation iconGuiElements = new ResourceLocation(NeedToBreathe.MODID, "textures/gui/guielements.png");

    public AirCompressorGui(AirCompressorTileEntity tileEntity, AirCompressorContainer container) {
        super(NeedToBreathe.instance, NTBMessages.INSTANCE, tileEntity, container, /*NeedToBreathe.GUI_MANUAL_MAIN*/ -1, "air_compressor");
        GenericEnergyStorageTileEntity.setCurrentRF(tileEntity.getEnergyStored());

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();

        int maxEnergyStored = tileEntity.getMaxEnergyStored();
        energyBar = new EnergyBar(mc, this).setVertical()
                .setMaxValue(maxEnergyStored)
                .setLayoutHint(new PositionalLayout.PositionalHint(10, 7, 8, 54))
                .setTooltips("Amount of energy left", "for compressing air")
                .setShowText(false);
        energyBar.setValue(GenericEnergyStorageTileEntity.getCurrentRF());


        Widget toplevel = new Panel(mc, this).setBackground(iconLocation).setLayout(new PositionalLayout())
                .addChild(energyBar);
        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
        tileEntity.requestRfFromServer(NeedToBreathe.MODID);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();

        energyBar.setValue(tileEntity.getEnergyStored());
        tileEntity.requestRfFromServer(NeedToBreathe.MODID);
    }
}
