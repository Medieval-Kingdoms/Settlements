package com.huskydreaming.settlements.utilities;

import com.google.common.base.Functions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Locale implements Parseable {
    PREFIX("&aSettlements: &7"),
    ADMIN_HELP(Arrays.asList(
            "&cAdmin Help:",
            "",
            "&f/admin claim [settlement] &8| &7Claims land for a settlement",
            "&f/admin disband [settlement] &8| &7Disbands a settlement",
            "&f/admin help &8| &7Displays admin help pages",
            "&f/admin setdescription [settlement] [description] &8| &7Sets a description for settlement",
            "&f/admin setowner [settlement] [player] &8| &7Sets an owner for a settlement",
            "&f/admin unclaim &8| &7Unclaims land for a settlement"
    )),
    SETTLEMENT_CREATED("You have created a new settlement named &b{0}&7."),
    SETTLEMENT_DESCRIPTION("You have set the settlement description to: &b{0}"),
    SETTLEMENT_DESCRIPTION_LONG("The description can't be greater than &b{0} &7characters."),
    SETTLEMENT_DISBAND("You have disbanded the settlement."),
    SETTLEMENT_ESTABLISHED("A settlement has already been established here."),
    SETTLEMENT_EXIST("A settlement with that name already exists."),
    SETTLEMENT_JOIN("You have joined the &b{0} &7settlement."),
    SETTLEMENT_JOIN_PLAYER("&b{0} &7has joined the settlement."),
    SETTLEMENT_KICK("You have been kicked from the &b{0} &7settlement."),
    SETTLEMENT_KICK_PLAYER("You have kicked &b{0} &7from the settlement."),
    SETTLEMENT_KICK_EXEMPT("This player can't be kicked from the settlement."),
    SETTLEMENT_LAND_ADJACENT("You must claim land that is adjacent to the settlement."),
    SETTLEMENT_LAND_CLAIMED("This land has already been claimed."),
    SETTLEMENT_LAND_CLAIMED_MAX("Your settlement can't claim more than &b{0}&7 piece(s) of land."),
    SETTLEMENT_LAND_NOT_CLAIMED("This land has not been claimed."),
    SETTLEMENT_LAND_CLAIM("You have claimed new land &7x: &b{0}&7, z: &b{1}"),
    SETTLEMENT_LAND_UNCLAIM("You have unclaimed the land from the settlement."),
    SETTLEMENT_LAND_UNCLAIM_ONE("The settlement only has only one claimed land. You can't unclaim more land for the settlement."),
    SETTLEMENT_LAND_WORLDGUARD("You can't claim this region as it is protected by worldguard."),
    SETTLEMENT_LEAVE("You have left the settlement."),
    SETTLEMENT_LEAVE_PLAYER("&b{0} &7has left the settlement."),
    SETTLEMENT_LEAVE_OWNER("You are not able to leave the settlement because you are the owner. You must transfer ownership first."),
    SETTLEMENT_NULL("The settlement &b{0} &7does not seem to exist."),
    SETTLEMENT_NOT_CITIZEN("The player is not a citizen of your settlement."),
    SETTLEMENT_OWNER("You have become the owner of your settlement."),
    SETTLEMENT_NOT_OWNER("You must be owner to proceed with this action."),
    SETTLEMENT_NOT_OWNER_TRANSFER("You must be the settlement owner to transfer ownership."),
    SETTLEMENT_IS_OWNER("You are already the owner of the settlement."),
    SETTLEMENT_OWNER_TRANSFERRED("You have transferred ownership of your settlement to &f{0}&7."),
    SETTLEMENT_PLAYER_EXISTS("You are already part of a settlement."),
    SETTLEMENT_PLAYER_HAS_SETTLEMENT("The player &b{0} &7already has a settlement."),
    SETTLEMENT_PLAYER_NULL("You do not seem to belong to a settlement."),
    SETTLEMENT_ROLE_CREATE("Successfully created the &b{0} &7role."),
    SETTLEMENT_ROLE_DELETE("Successfully deleted the &b{0} &7role."),
    SETTLEMENT_ROLE_EXISTS("The settlement already has the &b{0} &7role."),
    SETTLEMENT_ROLE_NULL("The role you are trying to delete doesn't exist."),
    SETTLEMENT_ROLE_ONE("The settlement must have at least one role available."),
    SETTLEMENT_SET_SPAWN("You have set the spawn for the settlement at your location."),
    SETTLEMENT_SPAWN("You have been teleported to the settlement spawn."),
    SETTLEMENT_TELEPORT("You have teleported to &7x: &b{0}&7, z: &b{1}"),
    INVITATION_DENIED("You have denied an invitation for &b{0}&7."),
    INVITATION_SELF("You can't invite yourself."),
    INVITATION_NULL("You do not have an invitation for &b{0}&7."),
    INVITATION_RECEIVED("You have been invited to join &b{0}&7. Do you wish to accept?"),
    INVITATION_SENT("You have sent an invitation to &b{0}&7."),
    INVITATION_ACCEPT("&a[Accept]"),
    INVITATION_DENY("&c[Deny]"),
    NO_PERMISSIONS("You do not have permissions for: &b{0}"),
    UNKNOWN_SUBCOMMAND("&b{0} &7is not a valid subcommand."),
    PLAYER_NULL("The player &b{0} &7has never played before."),
    PLAYER_OFFLINE("The player &b{0} &7does not seem to be online."),
    WILDERNESS_TITLE("&a&lWilderness"),
    WILDERNESS_FOOTER("&7Fresh new land awaits you");

    private final String def;

    private final List<String> list;
    private static FileConfiguration localeConfiguration;

    Locale(String def) {
        this.def = def;
        this.list = null;
    }

    Locale(List<String> list) {
        this.list = list;
        this.def = null;
    }

    @Nullable
    public String parse() {
        String message = localeConfiguration.getString(toString(), def);
        if(message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = localeConfiguration.getList(toString(), list);
        if(objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}
