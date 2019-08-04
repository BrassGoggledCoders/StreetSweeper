package xyz.brassgoggledcoders.streetsweeper;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;

public class CommandStreetSweeper extends CommandBase {

    @Override
    public String getName() {
        return "streetsweeper";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/streetsweeper";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        StreetSweeper.tryExecute(sender.getEntityWorld());
    }

}
