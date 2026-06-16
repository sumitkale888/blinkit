import React, { useEffect, useMemo, useState } from 'react';
import { FlatList, SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useNavigation, useRoute } from '@react-navigation/native';
import { appAxios } from '@service/apiInterceptor';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from '@components/ui/CustomText';
import ContentState from '@components/ui/ContentState';
import ProductCard from '@components/ui/ProductCard';

type RouteParams = {
  categoryId: string | number;
};

type ProductDto = {
  id: number | string;
  name?: string;
  description?: string;
  brand?: string;
  price?: number | string;
  stock?: number | string;
  categoryId?: number | string;
  images?: string[];
  thumbnailUrl?: string;
  averageRating?: number | string;
  rating?: number | string;
};

const CategoryProductsScreen = () => {
  const navigation = useNavigation<any>();
  const route = useRoute<any>();
  const { categoryId } = (route.params || {}) as RouteParams;

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [products, setProducts] = useState<ProductDto[]>([]);

  useEffect(() => {
    let mounted = true;

    const load = async () => {
      setLoading(true);
      setError(null);

      try {
        const res = await appAxios.get(`/products/category/${categoryId}`);
        const data = Array.isArray(res.data) ? res.data : res.data?.data ?? [];
        if (mounted) {
          setProducts(data);
        }
      } catch (e: any) {
        if (mounted) {
          setError(e?.response?.data?.message || e?.message || 'Failed to load products');
        }
      } finally {
        if (mounted) {
          setLoading(false);
        }
      }
    };

    if (categoryId !== undefined && categoryId !== null) {
      load();
    } else {
      setLoading(false);
      setError('Missing category id');
    }

    return () => {
      mounted = false;
    };
  }, [categoryId]);

  const title = useMemo(() => {
    return `Category ${categoryId ?? ''}`;
  }, [categoryId]);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backBtn}>
          <Text style={styles.backText}>{'<'} </Text>
        </TouchableOpacity>
        <CustomText variant="h5" fontFamily={Fonts.Bold} style={styles.title}>
          {title}
        </CustomText>
      </View>

      {loading ? (
        <ContentState status="loading" text="Loading products..." />
      ) : error ? (
        <ContentState status="error" text={error} />
      ) : products.length === 0 ? (
        <ContentState status="empty" text="No products found" />
      ) : (
        <FlatList
          data={products}
          keyExtractor={(item) => String(item.id)}
          contentContainerStyle={styles.listContent}
          renderItem={({ item }) => (
            <ProductCard
              product={item}
              compact={false}
              onPress={() => navigation.navigate('ProductDetailsScreen', { productId: item.id })}
            />
          )}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f6f8fb' },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingTop: 10,
    paddingBottom: 8,
    gap: 10,
  },
  backBtn: { paddingVertical: 8, paddingHorizontal: 4 },
  backText: { color: Colors.text, fontSize: 18, fontWeight: '700' },
  title: { color: Colors.text },
  listContent: { paddingHorizontal: 12, paddingBottom: 24 },
});

export default CategoryProductsScreen;
