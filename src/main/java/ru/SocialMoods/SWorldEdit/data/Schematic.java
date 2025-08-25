package ru.SocialMoods.SWorldEdit.data;

import lombok.Value;

@Value
public class Schematic {
    short width;
    short height;
    short length;
    byte[] blocks;
    byte[] data;
}
