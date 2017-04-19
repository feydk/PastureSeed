package io.github.feydk.pastureseed;

import com.winthier.custom.event.CustomRegisterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PastureSeedPlugin extends JavaPlugin implements Listener
{
    private static PastureSeedPlugin instance;
    private PastureSeed item;

    public PastureSeedPlugin()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

        item = new PastureSeed();
    }

    @EventHandler
    public void onCustomRegister(CustomRegisterEvent event)
    {
        event.addItem(item);

        new BukkitRunnable() {
            @Override public void run() {
                item.addRecipe();
            }
        }.runTask(this);
    }

    protected static PastureSeedPlugin getInstance()
    {
        return instance;
    }
}