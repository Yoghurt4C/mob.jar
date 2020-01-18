package mods.mobjar;

import mods.mobjar.mobjar.MobJarBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

import static mods.mobjar.MobJar.MOB_JAR;
import static mods.mobjar.MobJar.MOB_JAR_BLOCK_ENTITY;

public class MobJarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(MOB_JAR_BLOCK_ENTITY, MobJarBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), MOB_JAR);
    }
}