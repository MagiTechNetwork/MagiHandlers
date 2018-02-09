package net.heyzeer0.mgh;

import net.heyzeer0.mgh.api.bukkit.IBukkitEntity;
import net.heyzeer0.mgh.api.bukkit.IBukkitStack;
import net.heyzeer0.mgh.api.forge.IForgeEntity;
import net.heyzeer0.mgh.api.forge.IForgeStack;
import net.heyzeer0.mgh.api.forge.IForgeTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;

/**
 * Created by Frani on 02/11/2017.
 */
public class PhaseStack implements IBukkitStack, IForgeStack {

    private Deque<Object> phaseStack = new ArrayDeque<>(16);

    @Override
    public Optional<Player> getCurrentPlayer() {
        Player p = null;
        Optional<EntityPlayer> optional = getCurrentEntityPlayer();
        if (optional.isPresent()) {
            p = (Player) ((IBukkitEntity) optional.get()).getCraftEntity();
        }
        return Optional.ofNullable(p);
    }

    @Override
    public Optional<EntityPlayer> getCurrentEntityPlayer() {
        EntityPlayer p = null;
        if (getFirst(EntityPlayer.class).isPresent()) {
            p = getFirst(EntityPlayer.class).get();
        } else if (getFirst(IForgeTileEntity.class).isPresent()) {
            p = getFirst(IForgeTileEntity.class).get().getMHPlayer();
        } else if (getFirst(IForgeEntity.class).isPresent()) {
            p = getFirst(IForgeEntity.class).get().getOwner();
        }
        return Optional.ofNullable(p);
    }

    public boolean push(Object o) {
        return phaseStack.offerFirst(o);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getFirst(Class<T> c) {
        return (Optional<T>) phaseStack.stream().filter(c::isInstance).findFirst();
    }

    public boolean remove(Object o) {
        return phaseStack.removeIf(obj -> obj.equals(o));
    }

    public boolean isIgnoringPhase() {
        return this.ignorePhase;
    }
    public boolean ignorePhase = false;

    public void printThread() {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement e : Arrays.copyOfRange(Thread.currentThread().getStackTrace(), 2, 10)) {
            if(sb.length() != 0) sb.append('\n');
            sb.append(e);
        }
        System.out.println(sb);
    }


}
