package mods.mobjar.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxGoalMixin extends AnimalEntity {
    protected FoxGoalMixin(EntityType<? extends FoxEntity> entityType, World world){super(entityType, world);}

    @Inject(method="addTypeSpecificGoals", at=@At("HEAD"),cancellable = true)
    private void mobjar_dontAddTypeSpecificGoals(CallbackInfo ctx){
        if (world.isClient){ctx.cancel();}
    }
}
