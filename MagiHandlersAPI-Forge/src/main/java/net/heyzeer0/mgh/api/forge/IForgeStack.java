package net.heyzeer0.mgh.api.forge;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

/**
 * Created by Frani on 14/12/2017.
 */
public interface IForgeStack {

    Optional<EntityPlayer> getCurrentEntityPlayer();

}
