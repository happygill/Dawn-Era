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

    public Diet build(){
        return new Diet(foodItems,foodBlocks,foodEntities);
    }



    public record Diet(List<Item> foodItems, List<Block> foodBlocks, List<EntityType> foodEntities){

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
