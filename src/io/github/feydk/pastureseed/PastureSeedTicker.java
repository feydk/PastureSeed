package io.github.feydk.pastureseed;

import com.winthier.generic_events.GenericEventsPlugin;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PastureSeedTicker extends BukkitRunnable
{
    private Player player;
    private Block startingBlock;
    private List<Block> blocksToChange;
    private int radius = 3;
    private int speed = 5;
    private Random rnd;

    public PastureSeedTicker(Player player, Block block)
    {
        this.player = player;
        startingBlock = block;
        rnd = new Random(startingBlock.hashCode());
        blocksToChange = getBlocksToChange();
        Collections.shuffle(blocksToChange);
    }

    void start()
    {
        startingBlock.setType(Material.GRASS);
        startingBlock.getWorld().playSound(startingBlock.getLocation(), Sound.BLOCK_GRASS_BREAK, .1f, 1.0f);

        try
        {
            runTaskTimer(PastureSeedPlugin.getInstance(), speed, speed);
        }
        catch(IllegalStateException ise)
        {
            ise.printStackTrace();
        }
    }

    void stop()
    {
        try
        {
            cancel();
        }
        catch(IllegalStateException ise)
        {
            ise.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        if(blocksToChange.size() > 0)
        {
            Block block = blocksToChange.get(0);

            while(block.getType().equals(Material.GRASS) && blocksToChange.size() > 0)
            {
                blocksToChange.remove(0);

                if(blocksToChange.size() == 0)
                    break;

                block = blocksToChange.get(0);
            }

            block.setType(Material.GRASS);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, .1f, 1.0f);
            block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation(), 12, 0, 1, 0);
        }
        else
        {
            stop();
        }
    }

    @SuppressWarnings("deprecation")
    private List<Block> getBlocksToChange()
    {
        List<Block> range = new ArrayList<>();

        for(int x = startingBlock.getLocation().getBlockX() - radius; x <= startingBlock.getLocation().getBlockX() + radius; x++)
        {
            for(int y = startingBlock.getLocation().getBlockY() - 1; y <= startingBlock.getLocation().getBlockY() + 1; y++)
            {
                for(int z = startingBlock.getLocation().getBlockZ() - radius; z <= startingBlock.getLocation().getBlockZ() + radius; z++)
                {
                    Block block = startingBlock.getWorld().getBlockAt(x, y, z);

                    if(block.getType().equals(Material.DIRT) && block.getData() == 0 && block.getWorld().getBlockAt(x, y +1, z).getType().equals(Material.AIR) && GenericEventsPlugin.getInstance().playerCanGrief(player, block))
                        range.add(block);
                }
            }
        }

        List<Block> output = new ArrayList<>();

        for(Block block : range)
        {
            if(rnd.nextBoolean())
                output.add(block);

            /*List<Block> list = new ArrayList<>();

            for(int x = -1; x <= 1; x++)
            {
                for(int z = -1; z <= 1; z++)
                {
                    if(x == 0 && z == 0)
                        continue;

                    Block adjacent = block.getLocation().add(x, 0, z).getBlock();

                    if(adjacent.getType().equals(Material.DIRT) && adjacent.getData() == 0 && !list.contains(adjacent))
                        list.add(adjacent);
                }
            }

            if(!list.isEmpty())
            {
                //if(rnd.nextBoolean())
               // {
                    Block target = list.get(rnd.nextInt(list.size()));
                    output.add(target);
               // }
            }*/
        }

        return output;
    }
}