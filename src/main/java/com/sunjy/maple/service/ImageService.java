package com.sunjy.maple.service;

import com.sunjy.maple.model.CharacterData;

import java.awt.image.BufferedImage;

/**
 * @author created by sunjy on 12/1/23
 */
public interface ImageService {

    BufferedImage generateCharacterImage(CharacterData characterData);

}
