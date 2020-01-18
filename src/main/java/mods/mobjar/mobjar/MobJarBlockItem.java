package mods.mobjar.mobjar;

import mods.mobjar.MobJar;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class MobJarBlockItem extends BlockItem {

    public MobJarBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static boolean writeTagToBlockEntity(World world, PlayerEntity player, BlockPos pos, ItemStack stack) {
        MinecraftServer server = world.getServer();
        if (server != null) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be != null) {
                if (!world.isClient && be.shouldNotCopyTagFromItem() && (player == null || !player.isCreativeLevelTwoOp())) {
                    return false;
                }
                CompoundTag beWriteTag = be.toTag(new CompoundTag());
                beWriteTag.putInt("x", pos.getX());
                beWriteTag.putInt("y", pos.getY());
                beWriteTag.putInt("z", pos.getZ());
                if (stack.hasTag()) {
                    if (stack.getTag().contains("entityId")) {
                        beWriteTag.putString("entityId", stack.getTag().getString("entityId"));
                        beWriteTag.putString("entityName",stack.getTag().getString("entityName"));
                        if (stack.getTag().getCompound("entityData").contains("CustomName")){
                            beWriteTag.putString("customName",stack.getTag().getString("customName"));
                        }
                        CompoundTag rotatedEntityData = stack.getTag().getCompound("entityData");
                        ListTag rotationTag = new ListTag();
                        rotationTag.add(FloatTag.of((player.getHeadYaw()+180f)%360f));
                        rotationTag.add(FloatTag.of(360f-player.pitch));
                        rotatedEntityData.put("Rotation",rotationTag);
                        beWriteTag.put("entityData", rotatedEntityData);
                    }
                    be.fromTag(beWriteTag);
                }
            }

        }
        return false;
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.hasTag()){
            if (stack.getSubTag("entityData").contains("CustomName")) {
                return new TranslatableText(this.getTranslationKey(stack))
                        .append(" (")
                        .append(stack.getTag().getString("customName"))
                        .append(")");
            } else return new TranslatableText(this.getTranslationKey(stack))
                    .append(" (")
                    .append(stack.getTag().getString("entityName"))
                    .append(")");
        } else
        return new TranslatableText(this.getTranslationKey(stack));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> text, TooltipContext tooltipContext) {
        if (world != null) {
            if (stack.hasTag()) {
                assert stack.getTag() != null;
                text.add(new TranslatableText("mobjar.mobjar.type_tooltip").formatted(Formatting.GRAY)
                        .append(": ")
                        .append(new LiteralText(stack.getTag().getString("entityName")).formatted(Formatting.GOLD)));
                text.add(new TranslatableText("mobjar.mobjar.hp_tooltip").formatted(Formatting.GRAY)
                        .append(": ")
                        .append(new LiteralText(stack.getTag().getCompound("entityData").getFloat("Health")/2+"\u2764").formatted(Formatting.RED)));
                if (tooltipContext.isAdvanced()){
                    text.add(new TranslatableText("mobjar.mobjar.id_tooltip").formatted(Formatting.GRAY)
                            .append(": ")
                            .append(new LiteralText("\""+stack.getTag().getString("entityId")+"\"").formatted(Formatting.BLUE)));
                }
            } else {
                text.add(new TranslatableText("mobjar.mobjar.empty_tooltip").formatted(Formatting.GRAY));
            }
            text.add(new TranslatableText("mobjar.mobjar.break_tooltip").formatted(Formatting.DARK_GRAY,Formatting.ITALIC));
            text.add(new TranslatableText("mobjar.mobjar.break_tooltip_ps").formatted(Formatting.DARK_GRAY,Formatting.ITALIC));
        }
    }

    @Override
    public boolean useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (stack.getItem() == MobJar.MOB_JAR.asItem()) {
            ItemStack newItem = null;
            CompoundTag entityInfo = new CompoundTag();
            CompoundTag entityTag = new CompoundTag();
            if (!stack.hasTag()) {
                newItem = new ItemStack(MobJar.MOB_JAR.asItem());
                entity.toTag(entityTag);
                entityInfo.putString("entityName", entity.getType().getName().getString());
                if (entityTag.contains("CustomName")){entityInfo.putString("customName",entity.getDisplayName().asFormattedString());}
                entityInfo.putString("entityId", Registry.ENTITY_TYPE.getId(entity.getType()).toString());
                entityInfo.put("entityData", entityTag);
                newItem.setTag(entityInfo);
                entity.removed = true;
                stack.decrement(1);
            }
            if (newItem != null) {
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, newItem);
                } else player.inventory.offerOrDrop(player.getEntityWorld(),newItem);
            }
        }
        return true;
    }

    @Override
    public ActionResult place(ItemPlacementContext placementContext) {
        if (!placementContext.canPlace()) {
            return ActionResult.FAIL;
        } else {
            ItemPlacementContext newPlacementContext = this.getPlacementContext(placementContext);
            if (newPlacementContext == null) {
                return ActionResult.FAIL;
            } else {
                BlockState placementState = this.getPlacementState(newPlacementContext);
                if (placementState == null) {
                    return ActionResult.FAIL;
                } else if (!this.place(newPlacementContext, placementState)) {
                    return ActionResult.FAIL;
                } else {
                    BlockPos pos = newPlacementContext.getBlockPos();
                    World world = newPlacementContext.getWorld();
                    PlayerEntity player = newPlacementContext.getPlayer();
                    ItemStack stack = newPlacementContext.getStack();
                    BlockState newPlacementState = world.getBlockState(pos);
                    Block block = newPlacementState.getBlock();
                    if (block == placementState.getBlock()) {
                        this.postPlacement(pos, world, player, stack, newPlacementState);
                        block.onPlaced(world, pos, newPlacementState, player, stack);

                        if (player instanceof ServerPlayerEntity) {
                            Criterions.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
                        }
                    }

                    BlockSoundGroup placementSoundGroup = newPlacementState.getSoundGroup();
                    world.playSound(player, pos, this.getPlaceSound(newPlacementState), SoundCategory.BLOCKS, (placementSoundGroup.getVolume() + 1.0F) / 2.0F, placementSoundGroup.getPitch() * 0.8F);
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState placementState) {
        return writeTagToBlockEntity(world, player, pos, stack);
    }
}
