import React, { memo } from 'react';
import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from './CustomText';

type Product = {
  id: string | number;
  name?: string;
  description?: string;
  brand?: string;
  price?: number | string;
  stock?: number | string;
  thumbnailUrl?: string;
  images?: string[] | null;
  image?: { uri: string } | { [key: string]: any };
  rating?: number | string;
  averageRating?: number | string;
};

type Props = {
  product: Product;
  onPress?: () => void;
  onAddToCart?: () => void;
  showAddToCart?: boolean;
  compact?: boolean;
};

const getImageUri = (product: Product): { uri: string } | undefined => {
  if (product?.thumbnailUrl) {
    return { uri: product.thumbnailUrl };
  }
  if (Array.isArray(product?.images) && product.images.length > 0 && product.images[0]) {
    return { uri: product.images[0] };
  }
  // Some existing dummy mapping uses `image: { uri }`
  if (product?.image && typeof (product.image as any).uri === 'string') {
    return product.image as any;
  }
  return undefined;
};

const ProductCard = memo(({
  product,
  onPress,
  onAddToCart,
  showAddToCart = false,
  compact = false,
}: Props) => {
  const imageUri = getImageUri(product);

  const priceNumber = typeof product?.price === 'string' ? Number(product.price) : product?.price;
  const price = priceNumber != null && !Number.isNaN(Number(priceNumber)) ? Number(priceNumber) : null;

  const ratingVal = product?.averageRating ?? product?.rating;
  const ratingNumber = typeof ratingVal === 'string' ? Number(ratingVal) : ratingVal;
  const rating =
    ratingNumber != null && !Number.isNaN(Number(ratingNumber)) ? Number(ratingNumber) : null;

  return (
    <TouchableOpacity activeOpacity={0.9} onPress={onPress} style={[styles.card, compact && styles.compactCard]}>
      <View style={styles.imageWrap}>
        {imageUri ? (
          <Image source={imageUri} style={[styles.image, compact && styles.compactImage]} />
        ) : (
          <View style={[styles.imagePlaceholder, compact && styles.compactImagePlaceholder]} />
        )}
      </View>

      <View style={styles.content}>
        <CustomText
          variant="h6"
          style={styles.name}
          fontFamily={Fonts.SemiBold}
          numberOfLines={2}
        >
          {product?.name || 'Product'}
        </CustomText>

        {!!product?.brand && (
          <CustomText variant="body" style={styles.brand} fontFamily={Fonts.Medium} numberOfLines={1}>
            {product.brand}
          </CustomText>
        )}

        <View style={styles.priceRow}>
          {price != null ? (
            <CustomText variant="h6" style={styles.price} fontFamily={Fonts.Bold}>
              ₹{price}
            </CustomText>
          ) : (
            <CustomText variant="body" style={styles.priceMuted} fontFamily={Fonts.Medium}>
              Price
            </CustomText>
          )}
          {rating != null && (
            <View style={styles.ratingPill}>
              <Text style={styles.ratingText}>{rating.toFixed(1)}</Text>
            </View>
          )}
        </View>

        {showAddToCart && (
          <TouchableOpacity
            activeOpacity={0.9}
            onPress={onAddToCart}
            style={styles.addBtn}
          >
            <CustomText variant="h6" style={styles.addBtnText} fontFamily={Fonts.SemiBold}>
              Add
            </CustomText>
          </TouchableOpacity>
        )}
      </View>
    </TouchableOpacity>
  );
});

const styles = StyleSheet.create({
  card: {
    backgroundColor: 'white',
    borderRadius: 14,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.border,
    marginBottom: 12,
  },
  compactCard: {
    width: 160,
    marginRight: 12,
    marginBottom: 0,
  },
  imageWrap: {
    width: '100%',
    height: 120,
    backgroundColor: '#f5f6fb',
  },
  image: {
    width: '100%',
    height: '100%',
  },
  imagePlaceholder: {
    width: '100%',
    height: '100%',
    backgroundColor: '#f0f2f6',
  },
  compactImage: {
    height: 90,
  },
  compactImagePlaceholder: {
    height: 90,
  },
  content: {
    padding: 10,
  },
  name: {
    color: Colors.text,
    marginBottom: 2,
  },
  brand: {
    color: Colors.text,
    opacity: 0.7,
    marginBottom: 6,
  },
  priceRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  price: { color: '#f8890e' },
  priceMuted: { color: Colors.text, opacity: 0.7 },
  ratingPill: {
    backgroundColor: 'rgba(248,137,14,0.12)',
    paddingVertical: 4,
    paddingHorizontal: 8,
    borderRadius: 999,
  },
  ratingText: {
    color: '#f8890e',
    fontWeight: '700',
    fontSize: 12,
  },
  addBtn: {
    marginTop: 10,
    backgroundColor: Colors.secondary,
    borderRadius: 10,
    paddingVertical: 8,
    alignItems: 'center',
  },
  addBtnText: { color: 'white' },
});

export default ProductCard;
