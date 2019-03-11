package mcjty.needtobreathe.blocks;

import mcjty.needtobreathe.config.ConfigSetup;

public class PurifierTileEntity extends CommonPurifierTileEntity {

    public PurifierTileEntity() {
        super(ConfigSetup.PURIFIER_MAXRF, ConfigSetup.PURIFIER_RFINPUTPERTICK);
    }

}
