package com.example.animalcrossingdesign

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.toColor
import kotlin.math.abs
import kotlin.math.pow

fun getEuclideanSRGBDistance(color1: Color, color2: Color): Double {
    val redWeight = 1//0.3
    val greenWeight = 1//0.59
    val blueWeight = 1//0.11

    return (
            ((color2.red() - color1.red()) * redWeight).pow(2) +
                    ((color2.green() - color1.green()) * greenWeight).pow(2) +
                    ((color2.blue() - color1.blue()) * blueWeight).pow(2)).toDouble()
}


fun getClosestColor(color: Color, method: (Color, Color) -> Double): Pair<Color, HashMap<Color, Double>> {
    val acColors = AnimalCrossingQRObject.animalCrossingPalettePositionToColorMap.values

    var minDistance: Double = Double.MAX_VALUE
    var minDistanceColor: Color = color

    val distanceList = HashMap<Color, Double>()

    for (colorCode in acColors) {
        val acColor = colorCode.toInt().toColor()
        //val distance = getContrastRatio(color, acColor)
        //val distance = getEuclideanSRGBDistance(color, acColor)
        val distance = method(color, acColor)

        distanceList[acColor] = distance
        // What should happen if same distance?
        if (distance < minDistance) {
            minDistance = distance
            minDistanceColor = acColor
        }
    }
    return Pair(minDistanceColor, distanceList)
}

// TODO: rename
fun convertBitmapToFitACPalette(bitmap: Bitmap): Bitmap {
    val method = "rgb"
    val arrayListOfImageColors = IntArray(bitmap.width*bitmap.height)
    bitmap.getPixels(arrayListOfImageColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    // Todo
    // Add up total distances PER color in AC color palette and choose the 15 highest?/lowest?
    // <Color of pixel in image, <Color from AC palette, distance between them>>
    val colorDistanceMapOfMap = HashMap<Color, HashMap<Color, Double>>()

    // make array of already calculated distances for ACcolors and check against to reduce calculations
    val colorDistanceMap =  listOf<Color>()

    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width){
            val pixel = bitmap.getPixel(x, y)
            val pixelColor = pixel.toColor()
            //arrayListOfImageColors.add(pixelColor)

            if (colorDistanceMap.contains(pixelColor)) {
                continue
            }
            when (method) {
                "rgb" -> {
                    val closestColorInfo = getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                    val distanceListForThisPixel = closestColorInfo.second
                    colorDistanceMapOfMap[pixelColor] = distanceListForThisPixel
                }
                /*"contrast" -> {
                    val closestColorInfo = getClosestColor(pixelColor, ::getContrastRatio)
                    closestColor = closestColorInfo.first
                    val distanceListForThisPixel = closestColorInfo.second
                }
                else -> {
                    Toast.makeText(activity, "Color distance method not found. Using Euclidean sRGB Distance", Toast.LENGTH_LONG).show()
                    val closestColorInfo = getClosestColor(pixelColor, ::getEuclideanSRGBDistance)
                    closestColor = closestColorInfo.first
                    val distanceListForThisPixel = closestColorInfo.second
                }*/
            }
        }
    }

    // Is there a more effective way?
    val sumOfDifferencesForACColorPalette = HashMap<Color, Double>() // (Color in ACPalette, SumOfDifferences)
    for (distanceMap in colorDistanceMapOfMap.values) {
        for ((colorKey, distanceValue) in distanceMap) {
            sumOfDifferencesForACColorPalette[colorKey] = sumOfDifferencesForACColorPalette.getOrDefault(colorKey, 0.0) + distanceValue
        }
    }
    val orderedSumOfDifferencesACPalette = sumOfDifferencesForACColorPalette.entries.sortedWith(compareBy { it.value })
    val topFifteenColors = orderedSumOfDifferencesACPalette.slice(0..14)
    // TODO: turn topFifteenColors into list instead of list of map here
    //convertedImageColorPalettePositions = topFifteenColors.map { AnimalCrossingQRObject.animalCrossingPaletteColorToPositionMap[it.key.toArgb()]!! }.toList()

    // Using only 15 colors from AC palette
    val finalizedColorConversionMap =  HashMap<Color, Color>()
    for ((pixelColor, distanceMap) in colorDistanceMapOfMap) {
        val closestColorWithin15ColorPalette = distanceMap.filterKeys { it -> it in topFifteenColors.map { it.key } }.entries.sortedWith(compareBy { it.value })[0].key
        finalizedColorConversionMap[pixelColor] = closestColorWithin15ColorPalette
    }

    val recoloredImagePixels: IntArray = arrayListOfImageColors.map { finalizedColorConversionMap[it.toColor()]!!.toArgb() }.toList().toIntArray()

    //convertedImagePixels = recoloredImagePixels

    val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    newBitmap.setPixels(recoloredImagePixels, 0, bitmap.width, 0,0, bitmap.width, bitmap.height)

    return newBitmap
}

fun CreateQuickBitmap(colors: IntArray): Bitmap {
    // For color visualization when debugging
    return Bitmap.createBitmap(colors, colors.size, 1, Bitmap.Config.ARGB_8888)
}

fun ListColorToIntArray(list: List<Color>): IntArray {
    // For color visualization when debugging
    return list.map { it.toArgb() }.toIntArray()
}

@ExperimentalStdlibApi
fun convertBitmapMedianCut(bitmap: Bitmap): Bitmap {
    // TODO: Handle "transparent" color. Current converts to black?
    // TODO: When total distinct colors is 15 or less no need for buckets
    // TODO: Make buckets and create new color palette from whole(or larger than 32*32) before reducing to 32*32. Should make images nicer
    // assuming bitmap is 32x32 pixel bitmap
    val arrayListOfImageColors = IntArray(bitmap.width*bitmap.height)
    bitmap.getPixels(arrayListOfImageColors, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    assert(arrayListOfImageColors.size == 32*32)

    val firstPixel = arrayListOfImageColors[0].toColor()
    var red_min = firstPixel.red()
    var red_max = firstPixel.red()
    var green_min = firstPixel.green()
    var green_max = firstPixel.green()
    var blue_min = firstPixel.blue()
    var blue_max = firstPixel.blue()
    for (pixel in arrayListOfImageColors) {
        val color = pixel.toColor()
        if (color.red() < red_min) {
            red_min = color.red()
        } else if (color.red() > red_max) {
            red_max = color.red()
        }

        if (color.green() < green_min) {
            green_min = color.green()
        } else if (color.green() > green_max) {
            green_max = color.green()
        }

        if (color.blue() < blue_min) {
            blue_min = color.blue()
        } else if (color.blue() > blue_max) {
            blue_max = color.blue()
        }
    }

    val redRange = red_max - red_min
    val greenRange = green_max - green_min
    val blueRange = blue_max - blue_min

    val sortedPixels = if (redRange > greenRange && redRange > blueRange) {
        if (greenRange > blueRange) {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.red() }.thenBy { it.green() }.thenBy { it.blue() })
        } else {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.red() }.thenBy { it.blue() }.thenBy { it.green() })
        }
        //arrayListOfImageColors.map { it.toColor() }.sortedBy { it.red() }
    }else if (greenRange > redRange && greenRange > blueRange) {
        if (redRange > blueRange) {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.green() }.thenBy { it.red() }.thenBy { it.blue() })
        } else {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.green() }.thenBy { it.blue() }.thenBy { it.red() })
        }
    } else {
        if (redRange > greenRange) {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.blue() }.thenBy { it.red() }.thenBy { it.green() })
        } else {
            arrayListOfImageColors.map { it.toColor() }.sortedWith(compareBy<Color> { it.blue() }.thenBy { it.green() }.thenBy { it.red() })
        }
    }

    val PIXEL_MAX = 32*32
    val PALETTE_SIZE = 15
    val bucketSize: Int = PIXEL_MAX/PALETTE_SIZE

    val reducedARGBBucketPalette: MutableList<Int> = mutableListOf()
    for (i in 0 until bucketSize*PALETTE_SIZE step bucketSize) { // bucketSize*paletteSize instead of pixelMax to throw away remainder pixels

        // If last bucket, add in the remainder pixels
        var bucket: List<Color>
        if (i + PIXEL_MAX/PALETTE_SIZE == bucketSize*PALETTE_SIZE) {
            bucket = sortedPixels.toList().slice(i until sortedPixels.size)
            assert(bucket.size == bucketSize + 4)
            assert(abs(bucket.size - bucketSize) /bucketSize < 0.1)
        } else {
            bucket = sortedPixels.toList().slice(i until (i + bucketSize))
        }

        // average colors in the bucket
        val avg_red = (bucket.map { it.red() }.sum()) / bucket.size
        val avg_green = (bucket.map { it.green() }.sum()) / bucket.size
        val avg_blue = (bucket.map { it.blue() }.sum()) / bucket.size

        val bucketColorAverage = Color.argb(1f, avg_red,avg_green,avg_blue)
        reducedARGBBucketPalette.add(bucketColorAverage)
    }

    assert(reducedARGBBucketPalette.size == PALETTE_SIZE)

    val newBucketPaletteBindings = paletteToACPalette(reducedARGBBucketPalette)
    // Bindings for oldColor->acColor
    val newBindings: HashMap<Int, Int> = hashMapOf()
    for (oldColor in arrayListOfImageColors.distinct()) {
        val colorDistances = HashMap<Int, Double>()
        for (newPossibleColor in newBucketPaletteBindings.values.distinct()) {
            val distance = getEuclideanSRGBDistance(oldColor.toColor(), newPossibleColor.toColor())
            colorDistances[newPossibleColor] = distance
            val sortedColorDistances = colorDistances.entries.sortedWith(compareBy { it.value })
            newBindings[oldColor] = sortedColorDistances[0].key
        }

    }
    val recoloredImagePixels:  IntArray = arrayListOfImageColors.map {
        newBindings[it]!!
    }.toList().toIntArray()

    val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    newBitmap.setPixels(recoloredImagePixels, 0, bitmap.width, 0,0, bitmap.width, bitmap.height)

    return newBitmap
}

@ExperimentalStdlibApi
fun paletteToACPalette(palette: List<Int>): Map<Int, Int> {
    //    Returns a Hashmap<OldPaletteColor, NewACPaletteColor>
    assert(palette.size == 15)
    val acColors = AnimalCrossingQRObject.animalCrossingPalettePositionToColorMap.values

    val colorDistances = HashMap<Int, MutableList<MutableMap.MutableEntry<Int, Double>>>()
    for (old_color in palette) {
        println(colorDistances.size)
        val acColorDistances = HashMap<Int, Double>()
        for (acColor in acColors) {
            val distance = getEuclideanSRGBDistance(old_color.toColor(), acColor.toColor())
            acColorDistances[acColor] = distance
        }
        val sortedacColorDistances = acColorDistances.entries.sortedWith(compareBy { it.value })
        colorDistances[old_color] = sortedacColorDistances.toMutableList()
    }
    val newColorBindings = HashMap<Int, Int>() //  <new_color, owner_color(original_color)>
    for ((key, value) in colorDistances){
        newColorBindings[key] = value[0].key
    }
    return newColorBindings // (reduceColorMapColor, newPaletteColor)

    /* Save code for if 15 or fewer colors to create bindings for
    // Assign new palette transformation
    //val test: HashMap<Int, Int> // <Original_color, new_color>
    val versions = mutableListOf<HashMap<Int, Int>>()
    //val newColorBindings = HashMap<Int, Int>() //  <new_color, owner_color(original_color)>
    var counter = -1
    fun setacColorToBinding(acColor_to_test: Int, currentOriginalColor: Int, level: String): Boolean {
        counter += 1
        println(level + "mmmmmmmmmmmmmmmmmmmmmmmmm" + counter.toString())
        println(level + "ATTEMPT BIND $acColor_to_test to $currentOriginalColor")

        if (newColorBindings.containsKey(acColor_to_test)){
            /*
            If there is already a binding for this AC color
             */
            // Check that they are comparing distances against the same acColor
            //currentbindings shortest distance color == current colors shortest distance color
            //
            assert(colorDistances[newColorBindings[acColor_to_test]]!![0].key == acColor_to_test) // current binding
            assert(colorDistances[currentOriginalColor]!![0].key == acColor_to_test) // to_test binding
            assert(colorDistances[newColorBindings[acColor_to_test]]!![0].key == colorDistances[currentOriginalColor]!![0].key )
            // check which one is the closer color
            val current_bound_color = colorDistances[newColorBindings[acColor_to_test]]!![0]// color_distances for current mapping
            val this_color = colorDistances[currentOriginalColor]!![0]
            if (current_bound_color.value < this_color.value) { // Check their distances
                // check the next set
                println(level + "TRY NEXT")
                return false
            } else {
                // Handle the previously bound color
                // old binding
                val currentlyBoundOriginalColor_toHandle = newColorBindings[acColor_to_test]!!
                // Try to set the new binding. should i do this earlier?
                newColorBindings[acColor_to_test] = currentOriginalColor
                versions.add(newColorBindings)

                val testOldSize = colorDistances[currentlyBoundOriginalColor_toHandle]!!.size
                colorDistances[currentlyBoundOriginalColor_toHandle]!!.removeFirst()
                val testNewSize = colorDistances[currentlyBoundOriginalColor_toHandle]!!.size
                assert(testNewSize == testOldSize-1)

                //if (newColorBindings.containsKey(colorDistances[color_to_handle]!![0].key)) {
                //}
                //assert(colorDistances[color_to_handle]!![0].key == color_to_handl) // current binding
                println(level + "TRY RELOCATION")
                //println(colorDistances[currentlyBoundOriginalColor_toHandle]!![0].key)
                //println(currentlyBoundOriginalColor_toHandle)
                while (!setacColorToBinding(colorDistances[currentlyBoundOriginalColor_toHandle]!![0].key, currentlyBoundOriginalColor_toHandle, level+"---")) {
                    println(level + "REMOVE FIRST INNER")
                    colorDistances[currentlyBoundOriginalColor_toHandle]!!.removeFirst()
                }
                //setacColorToBinding(colorDistances[color_to_handle]!![0].key, color_to_handle)

                println(level + "SET REPLACE")
                println(level + "BOUND " + colorDistances[currentlyBoundOriginalColor_toHandle]!![0].key.toString() + " to " + currentlyBoundOriginalColor_toHandle.toString())
                return true
            }
        } else {
            //newColorBindings[acColor_to_test] = old_color
            newColorBindings[acColor_to_test] = currentOriginalColor
            versions.add(newColorBindings)

            println(level + "SET NEW")
            return true // is this right?
        }

    }

    println(colorDistances.size)
    //assert(false)
    for (old_color in colorDistances.keys) {
        val old_size = newColorBindings.size
        while (true) {
            val isSet = setacColorToBinding(colorDistances[old_color]!![0].key, old_color, "---")
            if (isSet) {
                println("SET NEW MAIN")
                println("BOUND " + colorDistances[old_color]!![0].key.toString() + " to " + old_color.toString())
                break
            } else {
                println("REMOVE FIRST OUTER")
                colorDistances[old_color]!!.removeFirst()
            }
        }
        assert(newColorBindings.size == old_size + 1)
    }

    for (version in versions) {
        println(version)
    }
    println(versions.size)
    println("SOSHELP!: " + newColorBindings.size.toString() + "/" + palette.size.toString())
    //assert(newColorBindings.size == palette.size)

    return newColorBindings.entries.associate { (key, value) -> value to key }
     */
}
