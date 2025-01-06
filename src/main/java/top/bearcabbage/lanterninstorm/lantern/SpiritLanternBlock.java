package top.bearcabbage.lanterninstorm.lantern;


import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import top.bearcabbage.lanterninstorm.player.PlayerAccessor;
import top.bearcabbage.lanterninstorm.player.Player;

public class SpiritLanternBlock extends LanternBlock implements Waterloggable {
    public static final MapCodec<SpiritLanternBlock> CODEC = createCodec(SpiritLanternBlock::new);
    public static final BooleanProperty STARTUP;


    public SpiritLanternBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(STARTUP, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        Player lsplayer = ((PlayerAccessor) player).getLS();
        if (!state.get(STARTUP)) {
            if (lsplayer.addLantern(world.getRegistryKey(), pos)) {
                state = state.cycle(STARTUP);
                world.setBlockState(pos, state, 3);
                return ActionResult.SUCCESS;
            }
            else return ActionResult.FAIL;
        } else {
            if (lsplayer.removeLantern(world.getRegistryKey(), pos)) {
                state = state.cycle(STARTUP);
                world.setBlockState(pos, state, 3);
                return ActionResult.SUCCESS;
            }
            else return ActionResult.FAIL;
        }
    }

    @Override


    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED, STARTUP);
    }

    static {
        STARTUP = BooleanProperty.of("startup");
    }

}