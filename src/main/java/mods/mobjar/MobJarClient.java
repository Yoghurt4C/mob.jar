package mods.mobjar;

import mods.mobjar.mobjar.MobJarBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

import static mods.mobjar.MobJar.*;

public class MobJarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(MOB_JAR_BLOCK_ENTITY, MobJarBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                MOB_JAR,
                WHITE_STAINED_MOB_JAR,
                ORANGE_STAINED_MOB_JAR,
                MAGENTA_STAINED_MOB_JAR,
                LIGHT_BLUE_STAINED_MOB_JAR,
                YELLOW_STAINED_MOB_JAR,
                LIME_STAINED_MOB_JAR,
                PINK_STAINED_MOB_JAR,
                GRAY_STAINED_MOB_JAR,
                LIGHT_GRAY_STAINED_MOB_JAR,
                CYAN_STAINED_MOB_JAR,
                PURPLE_STAINED_MOB_JAR,
                BLUE_STAINED_MOB_JAR,
                BROWN_STAINED_MOB_JAR,
                GREEN_STAINED_MOB_JAR,
                RED_STAINED_MOB_JAR,
                BLACK_STAINED_MOB_JAR
        );
    }
}