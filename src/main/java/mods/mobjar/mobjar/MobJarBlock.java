package mods.mobjar.mobjar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MobJarBlock extends Block implements BlockEntityProvider {
    public MobJarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new MobJarBlockEntity();
    }
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        world.playLevelEvent(player, 2001, pos, getRawIdFromState(state));
        if (!world.isClient()) {
            MobJarBlockEntity be =  (MobJarBlockEntity) world.getBlockEntity(pos);
            if (be != null) {
                if (be.getMyEntityType() != null) {
                    if (player.isSneaking()){
                        ItemStack stack = new ItemStack(this.asItem());
                        stack.getOrCreateTag().putString("entityId",be.getMyEntityType().toString());
                        stack.getOrCreateTag().putString("entityName",be.getEntityName());
                        if (be.getCustomName()!=null){
                            stack.getOrCreateTag().putString("customName",be.getCustomName());
                        }
                        stack.putSubTag("entityData",be.getMyEntityData());
                        dropStack(world,pos,stack);
                    } else {
                        Entity myEntity = Registry.ENTITY_TYPE.get(be.getMyEntityType()).create(world);
                        assert myEntity != null;
                        myEntity.fromTag(be.getMyEntityData());
                        myEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        world.spawnEntity(myEntity);
                        super.onBreak(world,pos,state,player);
                    }
                }
            }
        }
    }
}
