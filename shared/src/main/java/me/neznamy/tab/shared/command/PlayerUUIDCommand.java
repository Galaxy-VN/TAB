package me.neznamy.tab.shared.command;

import java.util.Arrays;
import java.util.List;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.TAB;

/**
 * Handler for "/tab playeruuid" subcommand
 */
public class PlayerUUIDCommand extends PropertyCommand {

	/**
	 * Constructs new instance
	 */
	public PlayerUUIDCommand() {
		super("playeruuid", null);
	}

	@Override
	public void execute(TabPlayer sender, String[] args) {
		//<uuid> <property> [value...]
		if (args.length <= 1) {
			help(sender);
			return;
		}

		String name = args[0];
		TabPlayer changed = TAB.getInstance().getPlayer(name);
		if (changed == null) {
			sendMessage(sender, getTranslation("player_not_found"));
			return;
		}
		String type = args[1].toLowerCase();
		String value = buildArgument(Arrays.copyOfRange(args, 2, args.length));
		if (type.equals("remove")) {
			if (hasPermission(sender, "tab.remove")) {
				TAB.getInstance().getConfiguration().getUsers().remove(changed.getUniqueId().toString());
				changed.forceRefresh();
				sendMessage(sender, getTranslation("data_removed").replace("%category%", "player").replace("%value%", changed.getName() + "(" + changed.getUniqueId().toString() + ")"));
			} else {
				sendMessage(sender, getTranslation("no_permission"));
			}
			return;
		}
		for (String property : getAllProperties()) {
			if (type.equals(property)) {
				if (hasPermission(sender, "tab.change." + property)) {
					savePlayer(sender, changed, type, value);
					if (extraProperties.contains(property) && !TAB.getInstance().getFeatureManager().isFeatureEnabled("nametagx")) {
						sendMessage(sender, getTranslation("unlimited_nametag_mode_not_enabled"));
					}
				} else {
					sendMessage(sender, getTranslation("no_permission"));
				}
				return;
			}
		}
		help(sender);
	}

	/**
	 * Saves new player settings into config
	 * @param sender - command sender or null if console
	 * @param player - affected player
	 * @param type - property type
	 * @param value - new value
	 */
	public void savePlayer(TabPlayer sender, TabPlayer player, String type, String value){
		if (value.length() > 0){
			sendMessage(sender, getTranslation("value_assigned").replace("%type%", type).replace("%value%", value).replace("%unit%", player.getName() + "(" + player.getUniqueId().toString() + ")").replace("%category%", "UUID"));
		} else {
			sendMessage(sender, getTranslation("value_removed").replace("%type%", type).replace("%unit%", player.getName() + "(" + player.getUniqueId().toString() + ")").replace("%category%", "UUID"));
		}
		String[] property = TAB.getInstance().getConfiguration().getUsers().getProperty(player.getUniqueId().toString(), type, null, null);
		if (property.length > 0 && String.valueOf(value.length() == 0 ? null : value).equals(String.valueOf(property[0]))) return;
		TAB.getInstance().getConfiguration().getUsers().setProperty(player.getUniqueId().toString(), type, null, null, value.length() == 0 ? null : value);
		player.forceRefresh();
	}
	
	@Override
	public List<String> complete(TabPlayer sender, String[] arguments) {
		if (arguments.length == 1) return getOnlinePlayers(arguments[0]);
		return super.complete(sender, arguments);
	}
}