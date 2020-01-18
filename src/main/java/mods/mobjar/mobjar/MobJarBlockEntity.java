package mods.mobjar.mobjar;

import mods.mobjar.MobJar;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

public class MobJarBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    private Entity entity;
    private Identifier myEntityType;
    private String entityName;
    private String customName;
    private CompoundTag entityData;
    private int tickCount = 0;

    public MobJarBlockEntity() { super(MobJar.MOB_JAR_BLOCK_ENTITY); }

    public CompoundTag toTag(CompoundTag tag) {
        if (myEntityType != null) {
            super.toTag(tag);
            CompoundTag ent = new CompoundTag();
            tag.put("entityData", entityData);
            tag.putString("entityName", entityName);
            if (entityData.contains("CustomName")){tag.putString("customName", customName);}
            tag.putString("entityId", myEntityType.toString());
        }
        return tag;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.myEntityType = new Identifier(tag.getString("entityId"));
        this.entityName = tag.getString("entityName");
        this.entityData = tag.getCompound("entityData");
        if (tag.getCompound("entityData").contains("CustomName")){this.customName = tag.getString("customName");}

    }


    public Entity getEntity() {
        if (entity == null) {
            if (myEntityType != null) {
                entity = Registry.ENTITY_TYPE.get(myEntityType).create(world);
                if(!world.isClient()){
                    world.getServer().getWorld(entity.dimension).spawnEntity(entity);
                }
                if(entity != null) {
                    entity.fromTag(entityData);
                    initializeTasks(entity);
                }
            }
        }
        return entity;
    }

    public void initializeTasks(Entity entity) {

    }

    public Identifier getMyEntityType(){ return myEntityType; }
    public CompoundTag getMyEntityData(){ return entityData; }
    public String getEntityName(){ return entityName;}
    public String getCustomName(){ return customName;}

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }

    public void tick() {
        if (entity != null) {
            //entity.getMoveControl().tick();
            entity.tick();
            if(!world.isClient){
                world.getServer().getWorld(entity.dimension).tickEntity(entity);
            }
        }
    }
}