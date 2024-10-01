package me.wang.menuShop;

import me.fmenu.fmenu.Fmenu;
import me.fmenu.fmenu.api.FmenuAPI;
import me.fmenu.fmenu.api.events.CommandExecEvent;
import me.fmenu.fmenu.api.events.PlayerOpenFmenuEvent;
import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class MenuShop extends JavaPlugin implements Listener {

    public static XConomyAPI xcapi;

    public static Map<Integer , String> messages = new HashMap<Integer, String>(Map.of(
            -1, "小于{0}",
            0 , "等于{0}",
            1 , "大于{0}",
            2 , "大于等于{0}",
            3 , "小于等于{0}"
    ));

    @Override
    public void onEnable() {
        // Plugin startup logic
        xcapi = new XConomyAPI();

        Fmenu fmenu = (Fmenu) Bukkit.getPluginManager().getPlugin("fmenu_for_1.21");
        if (fmenu != null) {
            FmenuAPI api = fmenu.getAPI();
            getLogger().info("FmenuVersion:"+api.getVersion());
        }else {
            getLogger().severe("未找到依赖：Fmenu");
        }


        getServer().getPluginManager().registerEvents(this,this);
        getLogger().info("格式：[Money,结果]:金钱");

        getLogger().info("结果列表：");
        getLogger().info("-1 小于设定金额");
        getLogger().info(" 0 等于设定金额");
        getLogger().info(" 1 大于设定金额");
        getLogger().info(" 2 大于等于设定金额");
        getLogger().info(" 3 小于等于设定金额");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler
    public void exec(CommandExecEvent e){
        Player player = e.getPlayer();

        if (!e.getArgs()[0].equalsIgnoreCase("Money")){
            return;
        }

        BigDecimal balance = xcapi.getPlayerData(player.getUniqueId()).getBalance();
        int result = balance.compareTo(new BigDecimal(e.getCommand()));

        if (result > -1 && e.getArgs()[1].equalsIgnoreCase("2")){
            result = 2;
        }
        if (result < 1 && e.getArgs()[1].equalsIgnoreCase("3")){
            result = 3;
        }

        if (!String.valueOf(result).equalsIgnoreCase(e.getArgs()[1])){
            player.sendMessage(ChatColor.RED+"你不符合条件："+messages.get(result).replace("{0}",e.getCommand()));
            e.setCancelled(true);
            e.setContinued(false);
        }
    }
}
