import React, { useEffect, useMemo, useState } from 'react';
import { Image, SafeAreaView, ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useNavigation, useRoute } from '@react-navigation/native';
import { appAxios } from '@service/apiInterceptor';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from '@components/ui/CustomText';
import ContentState from '@components/ui/ContentState';

type RouteParams = {
  productId: string | number;
};

type ProductDto = {
  id: number | string;
  name?: string;
  description?: string;
  brand?: string;
  price?: number | string;
  stock?: number | string;
  images?: string[];
  thumbnailUrl?: string;
  averageRating?: number | string;
  rating?: number | string;
  categoryId?: number | string;
};

const ProductDetailsScreen = () => {
  const navigation = useNavigation<any>();
  const route = useRoute<any>();
  const { productId } = (route.params || {}) as RouteParams;

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [product, setProduct] = useState<ProductDto | null>(null);

  useEffect(() => {
    let mounted = true;

    const load = async () => {
      setLoading(true);
      setError(null);

      try {
        const res = await appAxios.get(`/products/${productId}`);
        const data = res.data?.data ?? res.data;
        if (mounted) {
          setProduct(data);
        }
      } catch (e: any) {
        if (mounted) {
          setError(e?.response?.data?.message || e?.message || 'Failed to load product');
        }
      } finally {
        if (mounted) {
          setLoading(false);
        }
      }
    };

    if (productId !== undefined && productId !== null) {
      load();
    } else {
      setLoading(false);
      setError('Missing product id');
    }

    return () => {
      mounted = false;
    };
  }, [productId]);

  const imageUri = useMemo(() => {
    if (!product) return undefined;
    if (typeof product.thumbnailUrl === 'string') return { uri: product.thumbnailUrl };
    if (Array.isArray(product.images) && product.images.length > 0 && product.images[0]) {
      return { uri: product.images[0] };
    }
    return undefined;
  }, [product]);

  const price = useMemo(() => {
    if (!product?.price) return null;
    const n = typeof product.price === 'string' ? Number(product.price) : product.price;
    return Number.isNaN(Number(n)) ? null : Number(n);
  }, [product]);

  const stock = product?.stock ?? null;

  return (
    <SafeAreaView style={styles.container}>
      {loading ? (
        <ContentState status="loading" text="Loading product..." />
      ) : error ? (
        <ContentState status="error" text={error} />
      ) : !product ? (
        <ContentState status="empty" text="Product not found" />
      ) : (
        <>
          <ScrollView contentContainerStyle={styles.scrollContent}>
            <View style={styles.header}>
              <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backBtn}>
                <Text style={styles.backText}>{'<'} </Text>
              </TouchableOpacity>

              <CustomText variant="h5" fontFamily={Fonts.Bold} style={styles.title}>
                Product
              </CustomText>

              <View style={{ width: 40 }} />
            </View>

            <View style={styles.imageWrap}>
              {imageUri ? (
                <Image source={imageUri} style={styles.image} resizeMode="contain" />
              ) : (
                <View style={styles.imagePlaceholder} />
              )}
            </View>

            <CustomText variant="h5" fontFamily={Fonts.SemiBold} style={styles.name}>
              {product.name || 'Product'}
            </CustomText>

            <View style={styles.metaRow}>
              {!!product.brand && (
                <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.metaText}>
                  {product.brand}
                </CustomText>
              )}
              {price != null && (
                <CustomText variant="h6" fontFamily={Fonts.Bold} style={styles.priceText}>
                  ₹{price}
                </CustomText>
              )}
            </View>

            {product.averageRating != null && (
              <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.metaText}>
                Rating: {product.averageRating}
              </CustomText>
            )}

            <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.stockText}>
              {stock != null ? `Stock: ${stock}` : 'Stock unavailable'}
            </CustomText>

            <View style={styles.descBox}>
              <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.descTitle}>
                About
              </CustomText>
              <CustomText variant="body" fontFamily={Fonts.Regular} style={styles.descText}>
                {product.description || 'No description available'}
              </CustomText>
            </View>

            <View style={styles.bottomSpacer} />

            {/* Placeholder for add-to-cart UI; backend cart integration comes in later step */}
            <View style={styles.cartBox}>
              <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.cartHint}>
                Add-to-cart will be wired after confirming backend cart endpoints.
              </CustomText>
            </View>
          </ScrollView>

          <View style={styles.fixedFooter}>
            <TouchableOpacity
              style={styles.addToCartBtn}
              activeOpacity={0.9}
              onPress={() => {
                // TODO: wire to cart endpoint once confirmed
                setError(null);
              }}
              disabled={false}
            >
              <CustomText variant="h6" fontFamily={Fonts.Bold} style={styles.addToCartText}>
                Add to Cart
              </CustomText>
            </TouchableOpacity>
          </View>
        </>
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f6f8fb' },
  scrollContent: { paddingHorizontal: 16, paddingBottom: 96 },
  header: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', paddingTop: 8, marginBottom: 8 },
  backBtn: { paddingVertical: 8, paddingHorizontal: 4 },
  backText: { color: Colors.text, fontSize: 18, fontWeight: '700' },
  title: { color: Colors.text },
  imageWrap: { height: 260, backgroundColor: 'white', borderRadius: 16, borderWidth: 1, borderColor: Colors.border, overflow: 'hidden' },
  image: { width: '100%', height: '100%' },
  imagePlaceholder: { flex: 1, backgroundColor: '#f0f2f6' },
  name: { marginTop: 12, marginBottom: 6, color: Colors.text },
  metaRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', gap: 10 },
  metaText: { color: Colors.text, opacity: 0.8 },
  priceText: { color: '#f8890e' },
  stockText: { marginTop: 8, color: Colors.text, opacity: 0.75 },
  descBox: { marginTop: 16, backgroundColor: 'white', borderRadius: 16, padding: 14, borderWidth: 1, borderColor: Colors.border },
  descTitle: { color: Colors.text, marginBottom: 8 },
  descText: { color: Colors.text, opacity: 0.9, lineHeight: 18 },
  bottomSpacer: { height: 12 },
  cartBox: {
    marginTop: 16,
    backgroundColor: 'white',
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    borderColor: Colors.border,
  },
  cartHint: { color: Colors.text, opacity: 0.8 },
  fixedFooter: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
    padding: 16,
    backgroundColor: 'transparent',
  },
  addToCartBtn: {
    backgroundColor: Colors.secondary,
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Colors.secondary,
  },
  addToCartText: { color: 'white' },
});

export default ProductDetailsScreen;
