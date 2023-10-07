package com.happsg.dawnera.entity.api;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.openjdk.nashorn.internal.ir.Block;

import java.util.ArrayList;
import java.util.List;

public class DietBuilder {

    List<Item> foodItems=new ArrayList<>();
    List<Block> foodBlocks=new ArrayList<>();
    List<EntityType> foodEntities=new ArrayList<>();

    boolean hasHunger=true;

    int hungerDrainMinutes=40;
    int itemFoodReplenish=5;
    int liveFoodReplenish=25;


    public static DietBuilder diet() {
        return new DietBuilder();
    }

    public DietBuilder addFoodItems(Item... foodItems){
        this.foodItems= List.of(foodItems);
        return this;
    }

    public DietBuilder addFoodBlock(Block... foodBlocks){
        this.foodBlocks= List.of(foodBlocks);
        return this;
    }

    public DietBuilder addFoodEntities(EntityType... foodEntities){
        this.foodEntities= List.of(foodEntities);
        return this;
    }

    public DietBuilder setHungerDrainMinutes(int hungerDrain){
        this.hungerDrainMinutes= hungerDrain;
        return this;
    }

    public DietBuilder setItemFoodReplenish(int itemFoodReplenish){
        this.itemFoodReplenish= itemFoodReplenish;
        return this;
    }
    public DietBuilder setLiveFoodReplenish(int liveFoodReplenish){
        this.liveFoodReplenish= liveFoodReplenish;
        return this;
    }

    public DietBuilder hasHungerDrain(boolean hasHunger){
        this.hasHunger= hasHunger;
        return this;
    }
    public Diet build(){
        return new Diet(foodItems,foodBlocks,foodEntities,hungerDrainMinutes,itemFoodReplenish,liveFoodReplenish,hasHunger);
    }


    public record Diet(List<Item> foodItems, List<Block> foodBlocks, List<EntityType> foodEntities, int hungerDrainMinutes, int itemFoodReplenish, int liveFoodReplenish,boolean hasHunger){

        public boolean isEmpty(){
            return foodItems.isEmpty()&&foodEntities.isEmpty()&&foodBlocks.isEmpty();
        }
        @Override
        public List<Item> foodItems() {
            return List.copyOf(foodItems);
        }

        @Override
        public List<Block> foodBlocks() {
            return List.copyOf(foodBlocks);
        }

        @Override
        public List<EntityType> foodEntities() {
            return List.copyOf(foodEntities);
        }
    }
}
