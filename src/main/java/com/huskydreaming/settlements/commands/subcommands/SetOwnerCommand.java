package com.huskydreaming.settlements.commands.subcommands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.Util;
import com.huskydreaming.settlements.commands.CommandLabel;
import com.huskydreaming.settlements.storage.persistence.Member;
import com.huskydreaming.settlements.storage.persistence.Settlement;
import com.huskydreaming.settlements.services.interfaces.MemberService;
import com.huskydreaming.settlements.services.interfaces.SettlementService;
import com.huskydreaming.settlements.storage.types.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAnnotation(label = CommandLabel.SET_OWNER, arguments = " [player]")
public class SetOwnerCommand implements PlayerCommandProvider {

    private final MemberService memberService;
    private final SettlementService settlementService;

    public SetOwnerCommand(HuskyPlugin plugin) {
        memberService = plugin.provide(MemberService.class);
        settlementService = plugin.provide(SettlementService.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length != 2) return;
        String string = strings[1];

        if (!memberService.hasSettlement(player)) {
            player.sendMessage(Message.PLAYER_NULL.prefix());
            return;
        }

        Member member = memberService.getCitizen(player);
        Settlement settlement = settlementService.getSettlement(member.getSettlement());
        if (!settlement.isOwner(player)) {
            player.sendMessage(Message.OWNER_NOT.prefix());
            return;
        }

        OfflinePlayer offlinePlayer = Util.getOfflinePlayer(string);
        if (offlinePlayer == null) {
            player.sendMessage(Message.PLAYER_NULL.prefix(string));
            return;
        }

        if (offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Message.OWNER_CURRENT.prefix());
            return;
        }

        Member offlineMember = memberService.getCitizen(offlinePlayer);
        if (!offlineMember.getSettlement().equalsIgnoreCase(member.getSettlement())) {
            player.sendMessage(Message.NOT_CITIZEN.prefix());
            return;
        }

        settlement.setOwner(offlinePlayer);
        player.sendMessage(Message.OWNER_TRANSFERRED.prefix(offlinePlayer.getName()));
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if(strings.length == 2 && memberService.hasSettlement(player)) {
            Member member = memberService.getCitizen(player);
            Settlement settlement = settlementService.getSettlement(member.getSettlement());
            if(settlement.isOwner(player)) {
                return memberService.getOfflinePlayers(member.getSettlement()).stream().map(OfflinePlayer::getName).toList();
            }
        }
        return List.of();
    }
}