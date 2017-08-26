package net.heyzeer0.mgh.mixins.ic2;

import ic2.core.ExplosionIC2;
import net.heyzeer0.mgh.hacks.BlockHelper;
import net.heyzeer0.mgh.mixins.MixinManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by Frani on 08/08/2017.
 */

@Pseudo
@Mixin(targets = "ic2/core/item/tool/EntityMiningLaser", remap = false)
public abstract class MixinEntityMiningLaser extends Entity {

    public MixinEntityMiningLaser(World w) {
        super(w);
    }

    @Shadow public EntityLivingBase owner;

    @Redirect(method = "explode", at = @At(value = "NEW", target = "Lic2/core/ExplosionIC2;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDFF)V"))
    public ExplosionIC2 setExploder(World world, Entity entity, double x, double y, double z, float power1, float drop) {
        return new ExplosionIC2(world, owner, x, y, z, power1, drop);
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setFire(I)V"))
    public void injectSetFire(Entity entity, int i) {
        if (!MixinManager.canAttack(owner instanceof EntityPlayer ? (EntityPlayer)owner : FakePlayerFactory.getMinecraft((WorldServer)entity.worldObj), entity)) {
            setDead();
            return;
        }
        entity.setFire(i);
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "NEW", target = "Lnet/minecraft/world/Explosion;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V"))
    public Explosion injectExplosion(World world, Entity exploder, double x, double y, double z, float size ) {
        return new Explosion(world, owner, x, y, z, size);
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z"))
    public boolean spawnItem(World world, Entity e) {
        BlockEvent.BreakEvent ev = MixinManager.generateBlockEvent((int)e.posX, (int)e.posY, (int)e.posZ, world, (owner instanceof EntityPlayer ? (EntityPlayer) owner : FakePlayerFactory.getMinecraft((WorldServer) world)));
        MinecraftForge.EVENT_BUS.post(ev);
        if(!ev.isCanceled()) {
            return world.spawnEntityInWorld(e);
        }
        setDead();
        return false;
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z"))
    public boolean setBlock(World world, int x, int y, int z, Block block, int meta, int mode) {
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, (owner instanceof EntityPlayer ? (EntityPlayer) owner : FakePlayerFactory.getMinecraft((WorldServer) world)));
        MinecraftForge.EVENT_BUS.post(e);
        if(!e.isCanceled()) {
            return world.setBlock(x, y, z, block, meta, mode);
        }
        setDead();
        return false;
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;dropBlockAsItemWithChance(Lnet/minecraft/world/World;IIIIFI)V"))
    public void dropBlockWithChance(Block block, World world, int x, int y, int z, int meta, float chance, int mode) {
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, (owner instanceof EntityPlayer ? (EntityPlayer) owner : FakePlayerFactory.getMinecraft((WorldServer) world)));
        MinecraftForge.EVENT_BUS.post(e);
        if(!e.isCanceled()) {
            block.dropBlockAsItemWithChance(world, x, y, z, meta, chance, mode);
            return;
        }
        setDead();
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockToAir(III)Z"))
    public boolean setBlockWithPlayer(World world, int x, int y, int z) {
        if(BlockHelper.setBlockToAirWithOwner(x, y, z, world, owner)) {
            return true;
        }
        setDead();
        return false;
    }

    @Redirect(method = "func_70071_h_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z", ordinal = 1))
    public boolean setBlock2(World world, int x, int y, int z, Block block, int meta, int mode) {
        BlockEvent.BreakEvent e = MixinManager.generateBlockEvent(x, y, z, world, (owner instanceof EntityPlayer ? (EntityPlayer) owner : FakePlayerFactory.getMinecraft((WorldServer) world)));
        MinecraftForge.EVENT_BUS.post(e);
        if(!e.isCanceled()) {
            return world.setBlock(x, y, z, block, meta, mode);
        }
        setDead();
        return false;
    }

}
