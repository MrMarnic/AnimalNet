package me.marnic.animalnet.items;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.api.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Copyright (c) 04.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtEntityItem extends BasicItem {
    public CaughtEntityItem(Settings settings) {
        super(settings, "caught_entity");
    }

    private Entity entity;

    public ItemStack createInstance(Entity e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);
        ServerWorld serverWorld = (ServerWorld) e.getEntityWorld();
        String name = findGoodName(e, serverWorld.getSaveHandler().getWorldDir());

        CompoundTag tag = new CompoundTag();

        tag.putString("animalName", EntityType.getId(e.getType()).toString());
        tag.putString("modName", EntityType.getId(e.getType()).getNamespace());
        tag.putString("fileName", name);


        if (e.hasCustomName()) {
            if (!(toString(e.getCustomName()).equalsIgnoreCase(toString(e.getDisplayName())))) {
                tag.putString("animalTag", toString(e.getCustomName()));
            } else {
                tag.remove("animalTag");
            }
        } else {
            tag.remove("animalTag");
        }
        tag.putString("location", "x:" + (int) e.getPos().getX() + " y:" + (int) e.getPos().getY() + " z:" + (int) e.getPos().getZ());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        tag.putString("date", formatter.format(date));

        if (e instanceof AnimalEntity) {
            int age = ((AnimalEntity) e).getBreedingAge();
            if (age < 0) {
                tag.putString("age", "Child");
            } else {
                tag.putString("age", "Adult");
            }
        }

        stack.setTag(tag);

        if (!e.hasCustomName()) {
            stack.setCustomName(new LiteralText("Caught " + toStringTranslate(e.getDisplayName())).formatted(Formatting.YELLOW));
        } else {
            stack.setCustomName(new LiteralText("Caught " + toStringTranslate(e.getCustomName())).formatted(Formatting.YELLOW));
        }

        try {
            CompoundTag newtag = new CompoundTag();
            entity.saveSelfToTag(newtag);

            File f = new File(serverWorld.getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + name + ".dat");

            f.createNewFile();

            NbtIo.writeCompressed(newtag, new FileOutputStream(f));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return stack;
    }

    private String findGoodName(Entity e, File world) {
        File f = new File(world.getPath() + "//animalData");

        return e.getUuid().toString();
    }

    private String toString(Text c) {
        return c.asFormattedString();
    }

    private String toStringTranslate(Text c) {
        return (c).asFormattedString();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext con) {
        World world = con.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ServerWorld serverWorld = world.getServer().getWorld(world.dimension.getType());
            ItemStack itemstack = con.getStack();
            BlockPos blockpos = con.getBlockPos();
            Direction enumfacing = con.getSide();
            BlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();


            BlockPos blockpos1;
            if (iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.offset(enumfacing);
            }

            if (itemstack.hasTag()) {

                EntityType<LivingEntity> entitytype = getType(itemstack.getTag());
                LivingEntity living = null;

                CompoundTag tag = itemstack.getTag();

                CompoundTag entTag = null;

                try {
                    File f = new File(serverWorld.getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + tag.getString("fileName") + ".dat");

                    if (f.exists()) {
                        entTag = NbtIo.readCompressed(new FileInputStream(f));
                    } else {
                        entTag = new CompoundTag();
                        sendError(con.getPlayer(), "Error: The file for this entity : \"" + f.getAbsolutePath() + "\" is missing!");
                    }

                    BaseText custName = null;

                    if (tag.contains("animalTag")) {
                        custName = (new LiteralText(tag.getString("animalTag")));
                    }


                    living = EntityUtil.createEntity(entitytype, world, null, custName, con.getPlayer(), blockpos1, SpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP);

                    //living = entitytype.create(world, null, custName, con.getPlayer(), blockpos1, SpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP);


                    double x = living.getX();
                    double y = living.getY();
                    double z = living.getZ();
                    float yaw = living.yaw;
                    float pitch = living.pitch;

                    living.fromTag(entTag);

                    //living.setPosition(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

                    living.setPositionAndAngles(x, y, z, yaw, pitch);


                    world.spawnEntity(living);

                    con.getPlayer().inventory.removeInvStack(con.getPlayer().inventory.selectedSlot);

                    f.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (entitytype == null || living != null) {
                    itemstack.increment(-1);
                }
            } else {
                sendError(con.getPlayer(), "Error: The caught entity has no data in it.");
            }

            return ActionResult.SUCCESS;
        }
    }

    private void setBlockPosFromTag(CompoundTag tag, BlockPos pos) {
        tag.put("Pos", toListTag(pos.getX(), pos.getY(), pos.getZ()));
    }

    protected ListTag toListTag(double... doubles_1) {
        ListTag listTag_1 = new ListTag();
        double[] var3 = doubles_1;
        int var4 = doubles_1.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            double double_1 = var3[var5];
            listTag_1.add(DoubleTag.of(double_1));
        }

        return listTag_1;
    }

    private EntityType getType(CompoundTag c) {
        String id = c.getString("animalName");
        return EntityType.get(id).get();
    }

    private void sendError(PlayerEntity player, String msg) {
        player.addChatMessage(new LiteralText("§4" + msg), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world_1, List<Text> tooltip, TooltipContext tooltipContext_1) {
        if (stack.hasTag()) {
            if (stack.getTag().contains("animalTag")) {
                tooltip.add(new LiteralText(stack.getTag().getString("animalTag")));
            }
            tooltip.add(new LiteralText(stack.getTag().getString("location")));
            tooltip.add(new LiteralText(stack.getTag().getString("date")));
            if (stack.getTag().contains("age")) {
                tooltip.add(new LiteralText("Age: " + stack.getTag().getString("age")));
            }
            if (stack.getTag().contains("modName")) {
                tooltip.add(new LiteralText("Mod: " + stack.getTag().getString("modName")));
            }
        }
    }

    public static void makeAdult(ItemStack stack, World world) {

        CompoundTag compound = CaughtEntityItem.getTagForEntityFromItem(stack, world);
        compound.putInt("Age", 0);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound, world);
    }

    public static void makeFakeAdult(ItemStack stack) {
        CompoundTag st = stack.getTag();

        st.putString("age", "Adult");

        stack.setTag(st);
    }

    public static void makeChild(ItemStack stack, World world) {

        CompoundTag compound = CaughtEntityItem.getTagForEntityFromItem(stack, world);
        compound.putInt("Age", -23000);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound, world);
    }

    public static void makeFakeChild(ItemStack stack) {
        CompoundTag st = stack.getTag();

        st.putString("age", "Child");

        stack.setTag(st);
    }

    public static CompoundTag getTagForEntityFromItem(ItemStack stack, World world) {
        CompoundTag tagCompound = stack.getTag();
        File f = new File(world.getServer().getWorld(world.dimension.getType()).getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            if (f.exists()) {
                return NbtIo.readCompressed(new FileInputStream(f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CompoundTag();
    }

    public static void writeTagForEntityFromItem(ItemStack stack, CompoundTag entity, World world) {
        CompoundTag tagCompound = stack.getTag();
        File f = new File(world.getServer().getWorld(world.dimension.getType()).getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            f.delete();
            NbtIo.writeCompressed(entity, new FileOutputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasEnchantmentGlint(ItemStack itemStack_1) {
        return true;
    }
}
