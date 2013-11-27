This is a stub article.



ModelMultiBase

- For the multi-model
	Multi-model managed by MMM_TextureManager inherits the minimum MMM_ModelMultiBase,
	And if it is created on the basis of certain procedures, making it possible to perform display in the corresponding MOD all.
	
- The default behavior of MMMLib
	Reads a file containing the following strings, find the file that is included in it.
		· MMMLib
		· LittleMaidMob
	
	Parses the file that is found, it is recognized as a texture to search the following directories.
		· / Mob / ModelMulti /
		· / Mob / littleMaid /
	
	Find the Jar files and client files that are found, and will be read as a multi-model MMM_ModelMultiBase inheritance class that contains the following strings.
		· ModelMulti_
		· ModelLittleMaid_
		※ There is no need to put the MMM_ to the top as MMM_ *.

	The search string, you can add to your own mod.


ModelLittleMaidBase

- Parent-child relationship of parts
	Parent-child relationship of each part has become as follows.

	+ - MainFrame @
		|
		+ - BipedTorso @
			|
			+ - BipedNeck @
			| |
			| + - BipedHead
			| | |
			| | + - HeadTop
			| | |
			| | + - HeadMount
			| |
			| + - BipedRightArm
			| | |
			| | + - Arms [0]
			| |
			| + - BiprdLeftArm
			| |
			| + - Arms [1]
			|
			+ - BipedBody
			|
			+ - BipedPelvic @
				|
				+ - Skirt
				|
				+ - BipedRightLeg
				|
				+ - BipedLeftLeg


ModelSmartMovingBase (not implemented)

- Parent-child relationship of parts
	Parent-child relationship of each part has become as follows.
	+ - BipedOuter @
		|
		+ - BipedTorso @
			|
			+ - BipedBreast @
			| |
			| + - BipedHead
			| | |
			| | + - BipedHeadwear
			| | |
			| | + - HeadTop
			| | |
			| | + - HeadMount
			| |
			| + - BipedRightShoulder @
			| | |
			| | + - BipedRightArm
			| | |
			| | + - Arms [0]
			| |
			| + - BipedLeftShoulder @
			| |
			| + - BiprdLeftArm
			| |
			| + - Arms [1]
			|
			+ - BipedBody
			|
			+ - BipedPelvic @
				|
				+ - BipedRightLeg
				|
				+ - BipedLeftLeg

