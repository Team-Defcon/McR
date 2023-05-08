package superscary.mcr.revelations.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.revelations.blocks.entity.BarrelEntity;
import superscary.mcr.revelations.blocks.entity.ModBlockEntities;
import superscary.mcr.revelations.blocks.machine.EntityMachineBase;

import java.util.stream.Stream;

public class BarrelBlock extends EntityMachineBase
{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(4, 0, 4, 12, 15, 12),
            Block.box(5, 0, 2, 11, 15, 3),
            Block.box(5, 0, 1, 11, 16, 2),
            Block.box(5, 0, 13, 11, 15, 14),
            Block.box(5, 0, 14, 11, 16, 15),
            Block.box(13, 0, 5, 14, 15, 11),
            Block.box(14, 0, 5, 15, 16, 11),
            Block.box(2, 0, 5, 3, 15, 11),
            Block.box(1, 0, 5, 2, 16, 11),
            Block.box(12, 0, 12, 13, 16, 13),
            Block.box(4, 0, 2, 5, 16, 3),
            Block.box(2, 0, 4, 3, 16, 5),
            Block.box(3, 0, 3, 4, 16, 4),
            Block.box(11, 0, 13, 12, 16, 14),
            Block.box(13, 0, 11, 14, 16, 12),
            Block.box(2, 0, 11, 3, 16, 12),
            Block.box(3, 0, 12, 4, 16, 13),
            Block.box(4, 0, 13, 5, 16, 14),
            Block.box(13, 0, 4, 14, 16, 5),
            Block.box(12, 0, 3, 13, 16, 4),
            Block.box(11, 0, 2, 12, 16, 3),
            Block.box(3, 0, 4, 4, 15, 12),
            Block.box(12, 0, 4, 13, 15, 12),
            Block.box(4, 0, 3, 12, 15, 4),
            Block.box(4, 0, 12, 12, 15, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public BarrelBlock (Properties properties)
    {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape (BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement (BlockPlaceContext pContext)
    {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate (BlockState pState, Rotation pRotation)
    {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror (BlockState pState, Mirror pMirror)
    {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    // BLOCK ENTITY


    @Override
    public RenderShape getRenderShape (BlockState p_49232_)
    {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use (BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit)
    {
        if (!pLevel.isClientSide)
        {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof BarrelEntity)
            {
                if (pPlayer.getItemInHand(pHand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent())
                {
                    pPlayer.getItemInHand(pHand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                        if (handler.getFluidInTank(0).isEmpty())
                        {
                            fillHandler(pPlayer, pHand, (BarrelEntity) entity);
                            entity.saveWithId();
                        }
                        else
                        {
                            extractHandler(pPlayer, pHand, (BarrelEntity) entity);
                            entity.saveWithId();
                        }
                    });
                }
                else
                {
                    BarrelEntity fhbe = (BarrelEntity) entity;
                    sendPlayerTankInfo(pPlayer, fhbe);
                }

            }
            else
            {
                throw new IllegalStateException("Container provider missing");
            }
        }

        playSound(pLevel, pPos, pPlayer);

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    private static void playSound (Level pLevel, BlockPos pPos, Player pPlayer)
    {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof BarrelEntity barrelEntity)
        {
            if (barrelEntity.getFluidTank().isEmpty())
            {
                pLevel.playSound(pPlayer, pPos, SoundEvents.BOAT_PADDLE_LAND, SoundSource.PLAYERS, 1f, 1f);
            }
            else if (!barrelEntity.getFluidTank().isEmpty())
            {
                pLevel.playSound(pPlayer, pPos, SoundEvents.BOAT_PADDLE_WATER, SoundSource.PLAYERS, 1f, 1f);
            }
        }

    }

    private static void sendPlayerTankInfo (Player pPlayer, BarrelEntity fhbe)
    {
        if (fhbe.getFluidTank().getFluid().isEmpty())
        {
            pPlayer.sendSystemMessage(Component.translatable("gui.mcr.barrel.empty"));
        }
        else pPlayer.sendSystemMessage(Component.translatable("gui.mcr.barrel", fhbe.getFluidTank().getFluid().getDisplayName(), fhbe.getFluidTank().getFluid().getAmount(), fhbe.getFluidTank().getCapacity()));

    }

    private static void fillHandler (Player pPlayer, InteractionHand pHand, BarrelEntity entity)
    {
        BarrelEntity fhbe = entity;
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent())
        {
            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                if (!fhbe.getFluidTank().isEmpty() && fhbe.getFluidTank().getFluidAmount() >= 1000)
                {
                    handler.fill(fhbe.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                    fhbe.getFluidTank().drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE);
                    if (stack.getCount() == 1)
                    {
                        pPlayer.setItemInHand(pHand, handler.getContainer());
                    }
                    else
                    {
                        pPlayer.getInventory().add(handler.getContainer());
                        pPlayer.setItemInHand(pHand, stack);
                    }
                    sendPlayerTankInfo(pPlayer, fhbe);
                }
            });
        }
        else sendPlayerTankInfo(pPlayer, fhbe);
    }

    private static void extractHandler (Player pPlayer, InteractionHand pHand, BarrelEntity entity)
    {
        BarrelEntity fhbe = entity;
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent())
        {
            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                int drainAmount = 1000;

                FluidStack fluidStack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if (fhbe.getFluidTank().isEmpty() || (fhbe.getFluidTank().getFluid().containsFluid(fluidStack) && fhbe.getFluidTank().getSpace() >= 1000))
                {
                    fluidStack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                    fhbe.fillTankWithFluid(fluidStack);
                    pPlayer.setItemInHand(pHand, handler.getContainer());
                    sendPlayerTankInfo(pPlayer, fhbe);
                }
            });
        }
        else sendPlayerTankInfo(pPlayer, entity);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity (BlockPos pos, BlockState state)
    {
        return new BarrelEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker (Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModBlockEntities.BARREL.get(), BarrelEntity::tick);
    }

    @Override
    public boolean canDropFromExplosion (BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
    {
        return false;
    }

}
