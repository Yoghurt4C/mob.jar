package mods.mobjar.mixins;

import mods.mobjar.MobJar;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BuiltinModelItemRenderer.class)
public class MobJarBlockItemRendererMixin {

    @Inject(method = "render", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void mobJarRender(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ctx, Item item) {
        if (item == MobJar.MOB_JAR_ITEM) {
            MinecraftClient client = MinecraftClient.getInstance();
            Block mobJar = MobJar.MOB_JAR;
            client.getBlockRenderManager().renderBlockAsEntity(mobJar.getDefaultState(),matrices,vertexConsumers,light,overlay);
            if (stack.hasTag()) {
                Identifier entityType = new Identifier(stack.getTag().getString("entityId"));
                CompoundTag entityTag = stack.getSubTag("entityData");
                matrices.push();
                //RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, (float) (light & 0xFFFF), (float) ((light >> 16) & 0xFFFF));
                Entity entityToRender = Registry.ENTITY_TYPE.get(entityType).create(client.world);
                entityToRender.setWorld(client.world);
                entityToRender.fromTag(entityTag);
                float g = 0.33F;
                float h = Math.max(entityToRender.getWidth(), entityToRender.getHeight());
                if ((double)h > 1.0D) {
                    g /= h;
                }
                matrices.translate(0.5, 0.1, 0.5);
                matrices.scale(g,g,g);
                client.getEntityRenderManager().render(entityToRender, 0, 0, 0, 0, 0f, matrices, vertexConsumers, light);
                matrices.pop();
            }
        }
    }
}
