package mcjty.needtobreathe.blocks;

import mcjty.needtobreathe.config.Config;
import mcjty.needtobreathe.data.DimensionData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancedPurifierTileEntity extends CommonPurifierTileEntity {

    public AdvancedPurifierTileEntity() {
        super(Config.ADVANCED_PURIFIER_MAXRF, Config.ADVANCED_PURIFIER_RFINPUTPERTICK);
    }

    @Override
    protected int getRfPerTick() {
        return Config.ADVANCED_PURIFIER_RFPERTICK;
    }

    @Override
    protected int purifyAir(DimensionData data, BlockPos pp) {
        int workdone = 0;
        workdone += data.fillCleanAir(pp.getX(), pp.getY(), pp.getZ());
        workdone += purifyDirection(data, pp, EnumFacing.DOWN, null);
        workdone += purifyDirection(data, pp, EnumFacing.UP, null);
        workdone += purifyDirection(data, pp, EnumFacing.SOUTH, EnumFacing.EAST);
        workdone += purifyDirection(data, pp, EnumFacing.NORTH, EnumFacing.WEST);
        workdone += purifyDirection(data, pp, EnumFacing.WEST, EnumFacing.SOUTH);
        workdone += purifyDirection(data, pp, EnumFacing.EAST, EnumFacing.NORTH);
        return workdone;
    }

    private int purifyDirection(DimensionData data, BlockPos p, @Nonnull EnumFacing facing, @Nullable EnumFacing alternative) {
        int workdone = 0;
        p = p.offset(facing);
        if (data.isValid(world, p)) {
            workdone += data.fillCleanAir(p);
        }
        int cnt = 2;
        while (data.isValid(world, p.offset(facing)) && cnt > 0) {
            p = p.offset(facing);
            cnt--;
        }
        workdone += data.fillCleanAir(p);

        if (alternative != null) {
            cnt = 2;
            while (data.isValid(world, p.offset(alternative)) && cnt > 0) {
                p = p.offset(alternative);
                cnt--;
            }
            workdone += data.fillCleanAir(p);
        }

        return workdone;
    }

}
