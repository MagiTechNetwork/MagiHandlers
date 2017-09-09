package net.heyzeer0.mgh.mixins.thaumictinkerer;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import thaumic.tinkerer.common.block.BlockMod;
import thaumic.tinkerer.common.core.handler.ConfigHandler;
import thaumic.tinkerer.common.dim.TeleporterBedrock;
import thaumic.tinkerer.common.dim.WorldProviderBedrock;

import java.util.Random;

/**
 * Created by HeyZeer0 on 09/09/2017.
 * Copyright © HeyZeer0 - 2016
 */
@Pseudo
@Mixin(targets = "thaumic/tinkerer/common/block.kami/BlockBedrockPortal", remap = false)
public abstract class MixinBlockBedrockPortal extends BlockMod {

    public MixinBlockBedrockPortal() {
        super(Material.portal);
    }

    @Overwrite
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity entity) {
        super.onEntityCollidedWithBlock(par1World, par2, par3, par4, entity);

        if (entity.worldObj.provider.isSurfaceWorld()) {

            if (entity instanceof EntityPlayer && !par1World.isRemote) {

                FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) entity, ConfigHandler.bedrockDimensionID, new TeleporterBedrock((WorldServer) par1World));
                if(entity.worldObj.getBlock(par2, 250, par4) == Blocks.bedrock) {
                    entity.worldObj.setBlock(par2, 250, par4, Blocks.air);
                }
                if (entity.worldObj.getBlock(par2, 251, par4) == Blocks.bedrock) {
                    entity.worldObj.setBlock(par2, 251, par4, Blocks.air);
                }
                if (entity.worldObj.getBlock(par2, 252, par4) == Blocks.bedrock) {
                    entity.worldObj.setBlock(par2, 252, par4, Blocks.air);
                }
                if (entity.worldObj.getBlock(par2, 253, par4) == Blocks.bedrock) {
                    entity.worldObj.setBlock(par2, 253, par4, Blocks.air);
                }
                if (entity.worldObj.getBlock(par2, 254, par4) == Blocks.bedrock) {
                    entity.worldObj.setBlock(par2, 254, par4, this);
                }
                ((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(par2 + 0.5, 251, par4 + 0.5, 0, 0);
            }
        } else if (entity.worldObj.provider instanceof WorldProviderBedrock) {
            if (entity instanceof EntityPlayer && !par1World.isRemote) {

                FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) entity, 0, new TeleporterBedrock((WorldServer) par1World));

                Random rand = new Random();

                int x = (int) entity.posX + rand.nextInt(100);
                int z = (int) entity.posZ + rand.nextInt(100);

                x -= 50;
                z -= 50;

                int y = 120;

                ((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(x + 0.5, y + 3, z + 0.5, 0, 0);
            }
        }
    }

}
