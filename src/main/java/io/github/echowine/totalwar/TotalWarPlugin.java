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
import org.spongepowered.api.command.args.GenericArguments;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import java.net.URL;

@Plugin(id = "totalwar", name = "Total War Plugin", version = "1.0", description = "A sexy description")
public class TotalWarPlugin {
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path default_config;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> config_manager;

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path private_config_dir;
	
	@Inject
	private Logger logger;

	/**
	 * Retrieve the logger
	 *
	 * @return 
	 */
	public Logger getLogger() {
	    return logger;
	}
	
	/**
	 * Retrieve the config path
	 *
	 * @return 
	 */
	public Path getConfigPath() {
	    return this.private_config_dir;
	}


	/**
	 * Constructor
	 */
	@Inject public TotalWarPlugin(Logger logger) {
	    this.logger = logger;
	}
	
	/**
	 * Called when server start
	 * 
	 * @param event
	 */
	@Listener public void onServerStart(GameStartedServerEvent event) {

		try{

			getLogger().info("Total War v1.0");
			
			ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(getConfigPath()).build();
			ConfigurationNode cfg;
			
		    cfg = loader.load();
		    
		    if(cfg.getNode("database","hostname").getString() == null){

			    URL jar_config = this.getClass().getResource("config.conf");
			    ConfigurationLoader<CommentedConfigurationNode> jar_loader = HoconConfigurationLoader.builder().setURL(jar_config).build();
			    
			    cfg = jar_loader.load();
			    loader.save(cfg);
			    
		    }

		    DataBase.setLogger(logger);
			DataBase.connect(
				cfg.getNode("database","hostname").getString(),
				cfg.getNode("database","username").getString(),
				cfg.getNode("database","password").getString(),
				cfg.getNode("database","database").getString()
			);
			
			Faction.ini();
			
			this.registerCommands(event);
			
		}catch(Exception e) {
		
			getLogger().error(e.getMessage());
		}
		
		
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
