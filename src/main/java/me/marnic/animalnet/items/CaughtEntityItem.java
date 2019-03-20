package me.marnic.animalnet.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.jna.platform.unix.X11;
import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.api.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.MainMenuScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;

import java.io.File;
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
        ServerWorld serverWorld = (ServerWorld)e.getEntityWorld();
        String name = findGoodName(e, serverWorld.getSaveHandler().getWorldDir());

        CompoundTag tag = new CompoundTag();

        tag.putString("animalName", EntityType.getId(e.getType()).toString());
        tag.putString("fileName", name);


        if(e.hasCustomName()) {
            if (!(toString(e.getCustomName()).equalsIgnoreCase(toString(e.getDisplayName())))) {
                tag.putString("animalTag", toString(e.getCustomName()));
            }else{
                tag.remove("animalTag");
            }
        }else{
            tag.remove("animalTag");
        }
        tag.putString("location", "x:" + e.getPos().getX() + " y:" + e.getPos().getY() + " z:" + e.getPos().getZ());
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

        stack.addEnchantment(Enchantments.UNBREAKING, 1);

        if(!e.hasCustomName()) {
            stack.setDisplayName(new StringTextComponent("Caught " + toStringTranslate(e.getDisplayName())));
        }else{
            stack.setDisplayName(new StringTextComponent("Caught " + toStringTranslate(e.getCustomName())));
        }

        try {
            CompoundTag newtag = new CompoundTag();
            entity.saveSelfToTag(newtag);

            File f = new File(serverWorld.getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + name + ".dat");

            f.createNewFile();

            NbtIo.write(newtag, f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        AnimalEntity ee;

        return stack;
    }

    private String findGoodName(Entity e, File world) {
        File f = new File(world.getPath() + "//animalData");

        return e.getUuid().toString();
    }

    private String toString(TextComponent c) {
        System.out.println(c.getString());
        return ((StringTextComponent) c).getText();
    }

    private String toStringTranslate(TextComponent c) {
        System.out.println(c.getString());
        return ( c).getFormattedText();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext con) {
        World world = con.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            ServerWorld serverWorld = world.getServer().getWorld(world.dimension.getType());
            ItemStack itemstack = con.getItemStack();
            BlockPos blockpos = con.getBlockPos();
            Direction enumfacing = con.getFacing();
            BlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();


            BlockPos blockpos1;
            if (iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.offset(enumfacing);
            }

            if(itemstack.hasTag()) {

                EntityType<LivingEntity> entitytype = getType(itemstack.getTag());
                LivingEntity living = null;

                CompoundTag tag = itemstack.getTag();

                CompoundTag entTag = null;

                try {
                    File f = new File(serverWorld.getSaveHandler().getWorldDir().getAbsolutePath() + "//animalData//" + tag.getString("fileName") + ".dat");

                    if (f.exists()) {
                        entTag = NbtIo.read(f);
                    } else {
                        entTag = new CompoundTag();
                        sendError(con.getPlayer(), "Error: The file for this entity : \"" + f.getAbsolutePath() + "\" is missing!");
                    }

                    TextComponent custName = null;

                    if (tag.containsKey("animalTag")) {
                        custName = (new StringTextComponent(tag.getString("animalTag")));
                    }


                    living = EntityUtil.createEntity(entitytype,world, null, custName, con.getPlayer(), blockpos1, SpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP);

                    //living = entitytype.create(world, null, custName, con.getPlayer(), blockpos1, SpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP);


                    double x = living.x;
                    double y = living.y;
                    double z = living.z;
                    float yaw = living.yaw;
                    float pitch = living.pitch;

                    living.fromTag(entTag);

                    //living.setPosition(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

                    living.setPositionAndAngles(x,y,z,yaw,pitch);


                    world.spawnEntity(living);

                    con.getPlayer().inventory.removeInvStack(con.getPlayer().inventory.getSlotWithStack(con.getPlayer().inventory.getMainHandStack()));

                    f.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (entitytype == null || living != null) {
                    itemstack.subtractAmount(1);
                }
            }else{
                sendError(con.getPlayer(),"Error: The caught entity has no data in it.");
            }

            return ActionResult.SUCCESS;
        }
    }

    private void setBlockPosFromTag(CompoundTag tag,BlockPos pos) {
        tag.put("Pos",toListTag(pos.getX(),pos.getY(),pos.getZ()));
    }

    protected ListTag toListTag(double... doubles_1) {
        ListTag listTag_1 = new ListTag();
        double[] var3 = doubles_1;
        int var4 = doubles_1.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double double_1 = var3[var5];
            listTag_1.add(new DoubleTag(double_1));
        }

        return listTag_1;
    }

    private EntityType getType(CompoundTag c) {
        String id = c.getString("animalName");
        return EntityType.get(id).get();
    }

    private void sendError(PlayerEntity player, String msg) {
        player.addChatMessage(new StringTextComponent(ChatFormatting.RED + msg),true);
    }

    @Override
    public void buildTooltip(ItemStack stack, World world_1, List<TextComponent> tooltip, TooltipContext tooltipOptions_1) {
        if(stack.hasTag()) {
            if (stack.getTag().containsKey("animalTag")) {
                tooltip.add(new StringTextComponent(stack.getTag().getString("animalTag")));
            }
            tooltip.add(new StringTextComponent(stack.getTag().getString("location")));
            tooltip.add(new StringTextComponent(stack.getTag().getString("date")));
            if (stack.getTag().containsKey("age")) {
                tooltip.add(new StringTextComponent("Age: "+stack.getTag().getString("age")));
            }
        }
    }
}
