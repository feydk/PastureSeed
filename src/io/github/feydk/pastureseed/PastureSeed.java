package io.github.feydk.pastureseed;

import com.winthier.custom.CustomPlugin;
import com.winthier.custom.item.CustomItem;
import com.winthier.custom.item.ItemContext;
import com.winthier.custom.item.ItemDescription;
import com.winthier.custom.util.Dirty;
import com.winthier.custom.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class PastureSeed implements CustomItem
{
    private String displayName = "Pasture Seed";
    private String description = "Turns regular dirt into grass.";

    PastureSeed()
    {}

    @Override
    public String getCustomId()
    {
        return "pastureseed:pastureseed";
    }

    @Override
    public ItemStack spawnItemStack(int amount)
    {
        ItemStack item = getItem();
        item.setAmount(amount);

        return item;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event, ItemContext context)
    {
        if(event.getClickedBlock().getType().equals(Material.DIRT) && event.getClickedBlock().getData() == 0)
        {
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                event.setCancelled(true);
                PastureSeedTicker ticker = new PastureSeedTicker(event.getPlayer(), event.getClickedBlock());
                ticker.start();
                context.getItemStack().setAmount(context.getItemStack().getAmount() - 1);
            }
        }
    }

    @SuppressWarnings("deprecation")
    protected void addRecipe()
    {
        ShapedRecipe recipe = new ShapedRecipe(CustomPlugin.getInstance().getItemManager().spawnItemStack(getCustomId(), 1));

        recipe.shape("SSS", "SBS", "SSS");
        recipe.setIngredient('S', Material.SEEDS);
        recipe.setIngredient('B', Material.INK_SACK, 15);

        Bukkit.addRecipe(recipe);
    }

    private ItemStack getItem()
    {
        ItemStack item = new ItemStack(Material.SEEDS, 1);
        Dirty.setCustomId(item, getCustomId());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Msg.format("&a%s", this.displayName));
        item.setItemMeta(meta);

        ItemDescription description = new ItemDescription();
        description.setDescription(this.description);
        description.setUsage(Msg.format("&7[&2Right click&7]&r on a dirt block."));
        description.apply(item);

        return item;
    }
}