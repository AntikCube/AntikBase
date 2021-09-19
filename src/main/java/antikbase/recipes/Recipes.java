package antikbase.recipes;

import antikbase.AntikBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Recipes {

    private AntikBase antikBase;


    private static ItemStack privateChest;

    public Recipes(AntikBase antikBase) {
        this.antikBase = antikBase;


        init();
    }

    private void init() {
        privateChest = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = privateChest.getItemMeta();
        itemMeta.setDisplayName("§7Coffre privé");
        privateChest.setItemMeta(itemMeta);

        recipes();
    }

    private void recipes() {
        NamespacedKey namespacedKey = new NamespacedKey(this.antikBase, "private_chest");
        ShapedRecipe r = new ShapedRecipe( namespacedKey, privateChest);

        r.shape(" H ", "HCH", " T ");

        r.setIngredient('C', Material.CHEST);
        r.setIngredient('H', Material.CHAIN);
        r.setIngredient('T', Material.TRIPWIRE_HOOK);

        Bukkit.addRecipe(r);
    }

    public static ItemStack getPrivateChest() {
        return privateChest;
    }

}
