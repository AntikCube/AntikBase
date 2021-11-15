package antikbase.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class StarterCommand implements CommandExecutor {

    private List<Player> hadStarterKit = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.isOp() && !(sender instanceof Player)) {
            return true;
        }

        if(args.length != 1 && args.length != 2) {
            sender.sendMessage("§cFaites : /" + command.getName() + " <pseudo>");
            return true;
        }

        Player target;
        try {
            target = Bukkit.getPlayer(args[0]);
            if(target == null)
                throw new Exception();
        } catch(Exception e) {
            sender.sendMessage("§cLe joueur n'existe pas");
            return true;
        }

        if(args.length == 2 && args[1].equals("--force")) {
            giveKit(target);
            return true;
        } else if(target.hasPlayedBefore()) {
            target.sendMessage("§cVous ne pouvez plus profiter du kit");
            return true;

        } else if(hadStarterKit.contains(target)) {
            if(sender instanceof Player)
                sender.sendMessage("§cCe joueur a déjà reçu le kit");
            return true;
        }

        giveKit(target);
        hadStarterKit.add(target);
        return true;
    }

    private void giveKit(Player player) {
        List<ItemStack> itemStackList = new ArrayList<>();
        // Tools
        itemStackList.add(new ItemStack(Material.WOODEN_SWORD));
        itemStackList.add(new ItemStack(Material.WOODEN_PICKAXE));
        itemStackList.add(new ItemStack(Material.WOODEN_AXE));
        itemStackList.add(new ItemStack(Material.WOODEN_SHOVEL));
        itemStackList.add(new ItemStack(Material.WOODEN_HOE));

        // Loot
        itemStackList.add(new ItemStack(Material.OAK_WOOD, 32));
        itemStackList.add(new ItemStack(Material.BREAD, 16));
        itemStackList.add(new ItemStack(Material.FLINT_AND_STEEL));

        // Tenue
        itemStackList.add(new ItemStack(Material.CHAINMAIL_HELMET));
        itemStackList.add(getLeatherItem(Material.LEATHER_CHESTPLATE, Color.fromBGR(94, 124, 22)));
        itemStackList.add(getLeatherItem(Material.LEATHER_LEGGINGS, Color.fromBGR(94, 124, 22)));
        itemStackList.add(getLeatherItem(Material.LEATHER_BOOTS, Color.BLACK));

        itemStackList.add(getPotion(PotionType.INSTANT_HEAL, 20, 5));
        itemStackList.add(getPotion(PotionType.NIGHT_VISION, 20*60*3, 1));

        player.getInventory().addItem(itemStackList.toArray(ItemStack[]::new));
    }

    private ItemStack getLeatherItem(Material material, Color color) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta(leatherArmorMeta);
        return itemStack;
    }

    private ItemStack getPotion(PotionType potionType, int duration, int amplifier) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta itemMeta = (PotionMeta) itemStack.getItemMeta();
        itemMeta.addCustomEffect(new PotionEffect(potionType.getEffectType(), duration, amplifier), true);
        itemMeta.setColor(potionType.getEffectType().getColor());
        itemMeta.setDisplayName("§fPotion of " + format(potionType.getEffectType().getName().toLowerCase()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String format(String str) {
        str = str.toLowerCase();

        if(str.contains("_")) {
            String[] strings = str.split("_");
            StringBuilder strBuilder = new StringBuilder();
            for(String string : strings)
                strBuilder.append(string.substring(0, 1).toUpperCase()).append(string.substring(1).toLowerCase() + " ");
            return strBuilder.toString();
        } else {
            return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

}
