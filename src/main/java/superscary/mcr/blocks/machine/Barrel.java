package superscary.mcr.blocks.machine;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import superscary.mcr.blocks.entity.BarrelEntity;
import superscary.mcr.blocks.entity.ModBlockEntities;

public class Barrel extends EntityMachineBase
{

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public Barrel (Properties properties)
    {
        super(properties);
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

                pPlayer.playSound(SoundEvent.createFixedRangeEvent(new ResourceLocation("entity.player.swim"), 5));
            }
            else
            {
                throw new IllegalStateException("Container provider missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
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
