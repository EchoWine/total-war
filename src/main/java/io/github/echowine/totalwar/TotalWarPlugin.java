package io.github.echowine.totalwar;

import com.google.inject.Inject;

import io.github.echowine.totalwar.database.*;
import io.github.echowine.totalwar.model.*;
import io.github.echowine.totalwar.commands.*;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;

import org.slf4j.Logger;


@Plugin(id = "totalwar", name = "Total War Plugin", version = "1.0", description = "A sexy description")
public class TotalWarPlugin {
	
	@Inject
	private Logger logger;
	
	public Logger getLogger() {
	    return logger;
	}
	
	@Inject
	public TotalWarPlugin(Logger logger) {
	    this.logger = logger;
	}
	
	@Listener
    public void onServerStart(GameStartedServerEvent event) {

		getLogger().info("Total War v1.0");
		
		DataBase.setLogger(logger);
		DataBase.connect("localhost","root","","totalwar");		
		Faction.ini();
		
		this.registerCommands(event);
	}
	
	/**
	 * Register all commands
	 * 
	 * @param event
	 */
	public void registerCommands(GameStartedServerEvent event){
		
		CommandSpec tw = CommandSpec.builder()
			    .permission("totalwar")
			    .description(Text.of("TotalWar - Commands"))
			    .child(this.getFactionCommand(), "faction", "f")
			    .build();

		Sponge.getCommandManager().register(this, tw, "totalwar", "tw");

	}
	
	/**
	 * Retrieve faction command
	 * 
	 * @return
	 */
	public CommandSpec getFactionCommand(){

		CommandSpec add = CommandSpec.builder()
			    .description(Text.of("TotalWar - Add a faction"))
			    .permission("totalwar.faction.add")
			    .arguments(GenericArguments.string(Text.of("name")))
			    .executor(new FactionAddCommand())
			    .build();

		return CommandSpec.builder()
			    .permission("totalwar.faction")
			    .description(Text.of("TotalWar - Manage factions"))
			    .child(add, "add", "a")
			    .build();
		
	}
	
}
