package com.Jeka8833.CommandToBlock;


import java.util.ArrayList;
import java.util.List;

public class CommandToBlock {

    private final int chainLength;
    private final FacingWorld facing;
    private List<String> sCommand = new ArrayList<>();
    private List<String> lCommand = new ArrayList<>();

    public CommandToBlock() {
        this.chainLength = 32;
        this.facing = FacingWorld.West;
        sCommand.add("gamerule commandBlockOutput false");
    }

    public CommandToBlock(final int chainLength, final FacingWorld facing, final boolean commandBlockOutput) {
        this.chainLength = chainLength;
        this.facing = facing;
        if (commandBlockOutput) sCommand.add("gamerule commandBlockOutput false");
    }

    public CommandToBlock(final int chainLength, final FacingWorld facing, final boolean commandBlockOutput, final List<String> sCommand, final List<String> lCommand) {
        this.chainLength = chainLength;
        this.facing = facing;
        if (commandBlockOutput) sCommand.add("gamerule commandBlockOutput false");
        this.sCommand = sCommand;
        this.lCommand = lCommand;
    }

    public void addSingleCommand(final String command) {
        sCommand.add(command);
    }

    public void addRepeatCommand(final String command) {
        lCommand.add(command);
    }

    public List<String> getCommand() {
        final List<String> group = new ArrayList<>();
        int size = 0;
        boolean next = true;
        String mem = "";
        StringBuffer line = new StringBuffer();
        for (String command : sCommand) {
            if (next) {
                line = new StringBuffer();
                size = 127;
                line.append("summon falling_block ~ ~1 ~ {Block:redstone_block,Time:1,Passengers:[{id:falling_block,Block:activator_rail,Time:1,Passengers:[");
                if (!mem.equals("")) {
                    line.append(mem);
                    size += mem.length();
                }
                next = false;
            }
            final String text = "{id:commandblock_minecart,Command:\"" + command + "\"},";
            size += text.length();
            if (size > 32134) {
                mem = text;
                next = true;
                size = 0;
                line.append("{id:commandblock_minecart,Command:\"summon falling_block ~ ~2 ~ {Block:command_block,Time:1}\"},{id:commandblock_minecart,Command:\"setblock ~ ~ ~1 command_block 0 0 {Command:\\\"fill ~ ~-2 ~-1 ~ ~ ~ air\\\"}\"},{id:commandblock_minecart,Command:\"setblock ~ ~-1 ~1 redstone_block\"},{id:commandblock_minecart,Command:\"kill @e[type=commandblock_minecart,r=1]\"}]}]}");
                group.add(line.toString());
                continue;
            }
            line.append(text);
        }
        int x = 0;
        int z = 0;
        int facing = 0;
        int dis = 1;
        boolean rr = true;
        int count = 0;
        for (String command : lCommand) {
            if (next) {
                line = new StringBuffer();
                size = 127;
                line.append("summon falling_block ~ ~1 ~ {Block:redstone_block,Time:1,Passengers:[{id:falling_block,Block:activator_rail,Time:1,Passengers:[");
                if (!mem.equals("")) {
                    line.append(mem);
                    size += mem.length();
                }
                next = false;
            }
            int y = -2 + count / chainLength;
            switch (this.facing) {
                case North:
                    z = -(dis + 1);
                    facing = y % 2 == 0 ? 2 : 3;
                    break;
                case West:
                    x = -(dis + 1);
                    facing = y % 2 == 0 ? 4 : 5;
                    break;
                case South:
                    z = dis + 1;
                    facing = y % 2 == 0 ? 3 : 2;
                    break;
                case East:
                    x = dis + 1;
                    facing = y % 2 == 0 ? 5 : 4;
                    break;
            }
            final String text;
            if (count == 0) {
                text = "{id:commandblock_minecart,Command:\"setblock ~" + x + " ~" + y + " ~" + z + " repeating_command_block " + facing + " 0 {auto:1,Command:\\\"" + command + "\\\"}\"},";
            } else {
                if ((dis == 1 && !(y % 2 == 0)) || (dis == chainLength && y % 2 == 0)) {
                    text = "{id:commandblock_minecart,Command:\"setblock ~" + x + " ~" + y + " ~" + z + " chain_command_block 1 0 {auto:1,Command:\\\"" + command + "\\\"}\"},";
                } else {
                    text = "{id:commandblock_minecart,Command:\"setblock ~" + x + " ~" + y + " ~" + z + " chain_command_block " + facing + " 0 {auto:1,Command:\\\"" + command + "\\\"}\"},";
                }
            }
            size += text.length();
            if (rr) {
                dis++;
            }
            if (dis == chainLength + 1 || dis == 1) {
                rr = !rr;
            }
            if (!rr) {
                dis--;
            }

            count++;
            if (size > 32134) {
                mem = text;
                next = true;
                size = 0;
                line.append("{id:commandblock_minecart,Command:\"summon falling_block ~ ~2 ~ {Block:command_block,Time:1}\"},{id:commandblock_minecart,Command:\"setblock ~ ~ ~1 command_block 0 0 {Command:\\\"fill ~ ~-2 ~-1 ~ ~ ~ air\\\"}\"},{id:commandblock_minecart,Command:\"setblock ~ ~-1 ~1 redstone_block\"},{id:commandblock_minecart,Command:\"kill @e[type=commandblock_minecart,r=1]\"}]}]}");
                group.add(line.toString());
                continue;
            }
            line.append(text);
        }
        if (size != 0) {
            line.append("{id:commandblock_minecart,Command:\"setblock ~ ~ ~1 command_block 0 0 {Command:\\\"fill ~ ~-2 ~-1 ~ ~ ~ air\\\"}\"},{id:commandblock_minecart,Command:\"setblock ~ ~-1 ~1 redstone_block\"},{id:commandblock_minecart,Command:\"kill @e[type=commandblock_minecart,r=1]\"}]}]}");
            group.add(line.toString());
        }
        return group;
    }


}

enum FacingWorld {
    North,
    South,
    East,
    West,
}