package mods.mobjar.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import mods.mobjar.mobjar.MobJarBlockItem;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.arguments.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.opengl.GL13;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.WeakHashMap;

@Mixin(BuiltinModelItemRenderer.class)
public class MobJarBlockItemRendererMixin {
    public WeakHashMap<ItemStack, Entity> entityMap = new WeakHashMap<>();

    @Inject(method = "render", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void mobjar_mobJarRender(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ctx, Item item) {
        if (item instanceof MobJarBlockItem) {
            MinecraftClient client = MinecraftClient.getInstance();
            Block mobJar = Block.getBlockFromItem(stack.getItem());
            matrices.push();
            RenderSystem.enableCull();
            client.getBlockRenderManager().renderBlockAsEntity(mobJar.getDefaultState(),matrices,vertexConsumers,light,overlay);
            if (stack.hasTag() && stack.getSubTag("entityData")!=null) {
                float tickDelta = client.getTickDelta()+client.world.getTimeOfDay()*0.75f;
                Identifier entityType = new Identifier(stack.getTag().getString("entityId"));
                matrices.push();
                RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, (float) (light & 0xFFFF), (float) ((light >> 16) & 0xFFFF));
                entityMap.computeIfAbsent(stack,v -> {
                    Entity entityToRender = Registry.ENTITY_TYPE.get(entityType).create(client.world);
                    entityToRender.fromTag(stack.getSubTag("entityData"));
                    entityToRender.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entityToRender.getRotationVector());
                    return entityToRender;
                });
                Entity entityToRender = entityMap.get(stack);
                float g = 0.5F;
                float h = Math.max(entityToRender.getWidth(), entityToRender.getHeight());
                if ((double)h > 1.0D) {
                    g /= h;
                }
                matrices.translate(0.5, 0, 0.5);
                matrices.scale(g,g,g);
                client.getEntityRenderManager().render(entityToRender, 0, 0, 0, 0, tickDelta, matrices, vertexConsumers, light);
                matrices.pop();
            }
            matrices.pop();
        }
    }
}
