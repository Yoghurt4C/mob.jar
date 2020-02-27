package mods.mobjar.mobjar;

import net.minecraft.block.Stainable;
import net.minecraft.util.DyeColor;

public class StainedMobJarBlock extends MobJarBlock implements Stainable {
    private final DyeColor color;

    public StainedMobJarBlock(DyeColor color, Settings settings){
        super(settings);
        this.color=color;
    }

    public DyeColor getColor() {return this.color;}
}
