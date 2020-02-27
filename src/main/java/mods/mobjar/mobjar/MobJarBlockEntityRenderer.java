package mods.mobjar.mobjar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.arguments.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL13;

import java.util.WeakHashMap;

public class MobJarBlockEntityRenderer extends BlockEntityRenderer<MobJarBlockEntity> {
    public WeakHashMap<MobJarBlockEntity, Entity> entityMap = new WeakHashMap<>();

    public MobJarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(MobJarBlockEntity mobJar, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos pos = mobJar.getPos();
        if (mobJar.getEntity()!=null) {
            matrices.push();
            RenderSystem.glMultiTexCoord2f(GL13.GL_TEXTURE1, (float) (light & 0xFFFF), (float) ((light >> 16) & 0xFFFF));
            entityMap.computeIfAbsent(mobJar, v -> {
                Entity entityToRender = mobJar.getEntity();
                CompoundTag repositionedEntityData = mobJar.getMyEntityData();
                ListTag newPos = new ListTag();
                newPos.add(DoubleTag.of(pos.getX()+0.5d));
                newPos.add(DoubleTag.of(pos.getY()-0.15d));
                newPos.add(DoubleTag.of(pos.getZ()+0.5d));
                repositionedEntityData.put("Pos",newPos);
                entityToRender.fromTag(repositionedEntityData);
                return entityToRender;
            });
            Entity entityToRender = entityMap.get(mobJar);
            entityToRender.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, client.player.getCameraPosVec(tickDelta));
            float g = 0.5F;
            float h = Math.max(entityToRender.getWidth(), entityToRender.getHeight());
            if ((double) h > 1.0D) {
                g /= h;
            }
            matrices.translate(0.5, 0, 0.5);
            matrices.scale(g, g, g);
            client.getEntityRenderManager().render(entityToRender, 0,0,0, 0f, client.getTickDelta()+client.world.getTimeOfDay()*0.75f, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
