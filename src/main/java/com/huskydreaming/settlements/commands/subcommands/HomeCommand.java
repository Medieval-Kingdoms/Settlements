package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.enumeration.RolePermission;
import com.huskydreaming.settlements.services.interfaces.HomeService;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.RoleService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.persistence.Home;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Role;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@CommandAnnotation(label = CommandLabel.HOME)
public class HomeCommand implements PlayerCommandProvider {

    private final HomeService homeService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final SettlementService settlementService;

    public HomeCommand(HuskyPlugin plugin) {
        homeService = plugin.provide(HomeService.class);
        memberService = plugin.provide(MemberService.class);
        roleService = plugin.provide(RoleService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Role role = roleService.getRole(member);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());


        if (!(role.hasPermission(RolePermission.HOME_TELEPORT) || settlement.isOwner(player))) {
            player.sendMessage(Message.GENERAL_NO_PERMISSIONS.prefix());
            return;
        }

        if (!homeService.hasHome(member.getSettlement(), strings[1])) {
            player.sendMessage(Message.HOME_NULL.prefix(Util.capitalize(strings[1])));
            return;
        }

        Home home = homeService.getHome(member.getSettlement(), strings[1]);
        player.teleport(home.location());
        player.sendMessage(Message.HOME_TELEPORT.prefix(Util.capitalize(home.name())));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length != 2) return List.of();
        if (!memberService.hasSettlement(player)) return List.of();

        Member member = memberService.getCitizen(player);
        return homeService.getHomes(member.getSettlement()).stream().map(Home::name).collect(Collectors.toList());
    }
}