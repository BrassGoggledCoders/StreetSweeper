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
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return SweeperConfig.anyoneMayExecute ? true : super.checkPermission(server, sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        StreetSweeper.tryExecute(sender.getEntityWorld());
    }

}
