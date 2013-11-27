For the specification of multi-model support additional texture pack

It is a specification for creating a package to add texture to a multi-model.
Please refer to if you want to add a texture that you created yourself.



Term
	Texture pack
		That of the directory that contains the texture of one set.
	Multi-model
		Standards for using the model in the form other than the standard.
	Texture package
		That of the zip file that contains the texture pack.
		It is possible to include multiple texture packs, a model.
		



Naming convention of texture
	File name of the texture has been named on the basis of certain rules.
	※ There is the case that it has not been implemented, such as a reserved value also is included.

	- (This is provided for compatibility with older specification, use is not recommended) Rule 1
		?. mod_littlemaid png:? textures for each color, the hexadecimal number of up to 0 ~ F
		maid for texture wild: mod_littlemaidw.png
		texture of armor,:?. mod_littlemaid_a0 png
					　0 size +0.1
					　1 size +0.5

	And regulations 2
		* _ Png:?.? Texture of each color is a hexadecimal number between 0 ~ F
		* _10.png: (Equivalent to * _3c.png) texture maid for wild
		* _1 Png: texture of armor,?
					　1 (equivalent to default_40.png) size 0.1
					　2 (equivalent to default_50.png) size 0.5
		* _13.png: (Something like the eyes of a spider) translucent texture which does not depend on the brightness of the surroundings,
					　It is otherwise applicable contract for maid, is _6?. Png.
		* _14.png: (Something like the eyes of a spider) translucent texture which does not depend on the brightness of the surroundings,
					　It is otherwise applicable maid for a wild, is _7?. Png.
		* _15.png: (Equivalent to default_80.png) translucent texture which does not depend on the brightness of the surroundings.
		* _16.png: (Equivalent to default_90.png) translucent texture which does not depend on the brightness of the surroundings.
		* _20.png: Texture of the GUI
		* _3 Png:?.? Texture of color maid for a wild, hexadecimal number between 0 ~ F
		(Default | *) _4 png:?.? Texture Damaged Armor 1, the hexadecimal number of up to 0-9
		(Default | *) _5 png:?.? Texture Damaged Armor 2, the hexadecimal number of up to 0-9
		* _6 Png:?. Translucent texture which does not depend on the brightness of the surroundings of the individual contract for maid.
					　• The corresponding to each color in hexadecimal up to 0 ~ F is.
		* _7 Png:?. Translucent texture which does not depend on the brightness of the surroundings of the individual, maid for wild.
					　• The corresponding to each color in hexadecimal up to 0 ~ F is.
		(Default | *) _8 png:?. Translucent texture which does not depend on the brightness of the surroundings of the individual, Damaged armor for one.
					　• The hexadecimal number from 0 to 9.
		(Default | *) _9 png:?. Translucent texture which does not depend on the brightness of the surroundings of the individual, Damaged armor for 2.
					　• The hexadecimal number from 0 to 9.

		*: Favorite name, but you can not use the Japanese.


	Of armor. Minecraft's Ex +1.0 +0.5
	Note: Armor texture will not work properly if you do not prepare in two pieces set.
	　　Use the default of those things that does not exist in the texture pack.


Naming convention texture pack (directory)
	There are certain rules to the directory that contains the texture.

	• You can not use periods in the directory name.

	• You can not use Japanese in the directory name.

	- By adding the model name "_naz", etc. at the end of the texture pack name,
	　It is possible that corresponds to the multi-model, which will be described later.

	- The texture pack name in the directory below a certain now
	　There is a need to create a directory.
	　specific directory name MMMLib reads in the standard are as follows.

		/ Mob / littleMaid /
		/ Mob / ModelMulti /

	　If the directory below, you should be able to use regardless of hierarchy.
	　(Thorough testing is performed actually only two layers)
	
	　Actual directory structure is as follows.

		/ Mob/littleMaid/naz_naz/naz_00.png
			.
			.
		/ Mob/littleMaid/naz_naz/naz_20.png


Multi-model
	By registering the texture package several model classes,
	It can be used at the same time the various models.

	Model class
		A class that inherits from "ModelMultiBase.class"
		You can use a special model shape by placing the texture pack.

	- Location of the model class
		Please place the root of the texture package.

			/ ModelLittleMaid_naz.class
			/ Mob / littleMaid / naz_naz / ~

	-Naming convention of the model class
		As a prefix of the lexical model class "ModelLittleMaid_"
		Or, please add "ModelMulti_".
		Underscore or later serve as a model name, name of the texture pack
		As a model for the texture pack by applying the very end
		Will be applied.

			ModelLittleMaid_naz.class
			ModelLittleMaid_chrno.class

			/ Mob / littleMaid / naz_naz / ~
			/ Mob / littleMaid / Cirno_chrno / ~
		
	You should make the attractions unique name of the model class so as not to conflict: Note.
	　　If the model class of the same name is there are a plurality,
	　　It should be the one that was last read. (Unconfirmed)

	Please refer to it so have been described in the text of another detail.



Package file
	When you throw in appdata% / .mincraft / mods /% files that are named in a certain naming convention,
	littleMaidMob Check the file automatically.

		like to name *:. littleMaidMob-* zip
		Like to name *:. ModelMulti-* zip

		Ex.littleMaidMob-texture.zip
	
	In addition, it supports multiple texture pack if that matches the naming convention.
	If a directory with the same name exists in the texture pack other,
	I will be overwritten with the texture that was loaded last.



Color maid of wild
	Conventional, brown color of maid of wild was (12) fixed, but it has been associated to each new color.
	Made of wild Select the texture pack at random as they occur,
	I set a random wild colors that are registered.
	You can use the texture of default if the texture of the maid of the wild has not been set at this time.
	I use the following items as a texture made of wild.

		* _3 Png:?.? Desired name, and * are hexadecimal numbers from up to 0 ~ F.

	Maid texture of wild that were set prior has been assigned to 3c internally.

		* _10.png → * _3c.png


Armor texture
	Specifications for the armor texture is added,
	It is supposed to be durable texture display each time, of each material.

	Damaged Armor
		If you set aside the armor texture of damage each, one corresponding to it will be displayed.
		Has become in increments of 10% set interval.

			* _40.png, * _50.png: Durability is displayed when less than 100%
			* _41.png, * _51.png: Durability is displayed when 90% or less
			* _42.png, * _52.png: Durability is displayed when 80% or less
			* _43.png, * _53.png: Durability is displayed when 70% or less
			* _44.png, * _54.png: Durability is displayed when less than 60%
			* _45.png, * _55.png: Durability is displayed when less than 50%
			* _46.png, * _56.png: Durability is displayed when less than 40%
			* _47.png, * _57.png: Durability is displayed when 30% or less
			* _48.png, * _58.png: Durability is displayed when less than 20%
			* _49.png, * _59.png: Durability is displayed when more than 10%

		Further, if the texture corresponding to the durability is not provided,
		Show me the texture of large time durability more recent.

		Durability of armor is at the 40% there is a texture Ex.40, of 43 and 47,
		   Using the texture of 43 Searches the 44 → 43 you want to select the 45 but because there.


	Armor material
		Because it is supposed to be able to set the texture of each material, the constraint has occurred to the file name.
		* It is necessary to be a particular string the name of the part of the.

			* _11.png → default_40.png
			* _12.png → default_50.png

			??. default_ png: standard armor texture,
					　This is displayed if the texture of each material does not exist.
			Armor texture of leather armor:??. cloth_ png
			Armor texture of iron armor:??. iron_ png
			Armor texture of golden armor:??. gold_ png
			Armor texture of diamond armor:??. diamond_ png

		I conform to the armor texture name of the player: Note.



Note
	• If the Japanese are included in the files in the package, the error seems to occur at startup.
	　ModLoader not to because this seems to be a problem according to character code processing of Java,
	　Please to avoid their own.
	- I'm going on Minecraft on Windows operation check.
	　There is a risk that may not work properly in other environments.
	-There is a possibility to be changed in the future specification.



History
	Moved from 20130618.1 littleMaidMob.
