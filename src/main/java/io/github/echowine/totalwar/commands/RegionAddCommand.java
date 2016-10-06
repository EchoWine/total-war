package io.github.echowine.totalwar.commands;

import io.github.echowine.totalwar.model.*;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class RegionAddCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("Added successfully"));
        
        String name = args.<String>getOne("name").get();
        
        Region region = new Region();
        region.setName(name);
        region.save();
        
        return CommandResult.success();
    }
}