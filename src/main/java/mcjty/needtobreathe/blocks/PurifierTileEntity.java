package mcjty.needtobreathe.blocks;

import mcjty.needtobreathe.config.Config;

public class PurifierTileEntity extends CommonPurifierTileEntity {

    public PurifierTileEntity() {
        super(Config.PURIFIER_MAXRF, Config.PURIFIER_RFINPUTPERTICK);
    }

}
