import React, { useEffect, useMemo, useState } from 'react';
import { ActivityIndicator, FlatList, Image, Pressable, SafeAreaView, StyleSheet, Text, View } from 'react-native';
import { useAuthStore } from '@state/authStore';
import { appAxios } from '@service/apiInterceptor';
import { logout } from '@service/authService';
import Button from '@components/ui/Button';
import ContentState from '@components/ui/ContentState';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from '@components/ui/CustomText';
import ProductCard from '@components/ui/ProductCard';
import { useNavigation } from '@react-navigation/native';

const ProductDashboard = () => {
  const navigation = useNavigation<any>();
  const { user } = useAuthStore();

  const [categories, setCategories] = useState<any[]>([]);
  const [products, setProducts] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError(null);

      try {
        const [categoryResponse, productResponse] = await Promise.all([
          appAxios.get('/categories'),
          appAxios.get('/products'),
        ]);

        const categoryData = Array.isArray(categoryResponse.data)
          ? categoryResponse.data
          : categoryResponse.data?.data ?? [];

        const productData = Array.isArray(productResponse.data)
          ? productResponse.data
          : productResponse.data?.data ?? [];

        setCategories(categoryData);
        setProducts(
          (productData || []).map((item: any) => ({
            id: item.id?.toString(),
            name: item.name,
            price: item.price,
            thumbnailUrl: item.thumbnailUrl,
            images: item.images,
            averageRating: item.averageRating,
            rating: item.rating,
          }))
        );
      } catch (e: any) {
        setError(e?.response?.data?.message || e?.message || 'Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const firstName = user?.firstName || user?.email || 'Customer';

  const featuredProducts = useMemo(() => products.slice(0, 10), [products]);

  const getCategoryImage = (cat: any): { uri: string } | undefined => {
    if (typeof cat?.imageUrl === 'string' && cat.imageUrl) {
      return { uri: cat.imageUrl };
    }
    if (typeof cat?.image === 'string' && cat.image) {
      return { uri: cat.image };
    }
    if (cat?.image?.uri) {
      return cat.image;
    }
    return undefined;
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <CustomText variant="h5" fontFamily={Fonts.Bold} style={styles.title}>
          Welcome back{firstName ? `, ${firstName}` : ''}!
        </CustomText>
        <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.subtitle}>
          Browse daily essentials
        </CustomText>
      </View>

      {loading ? (
        <ActivityIndicator size="large" color={Colors.primary} style={styles.loader} />
      ) : error ? (
        <ContentState status="error" text={error} />
      ) : (
        <>
          <View style={styles.section}>
            <CustomText variant="h6" fontFamily={Fonts.Bold} style={styles.sectionTitle}>
              Top Categories
            </CustomText>

            <FlatList
              data={categories}
              horizontal
              showsHorizontalScrollIndicator={false}
              keyExtractor={(item) => String(item.id)}
              renderItem={({ item }) => {
                const imageUri = getCategoryImage(item);
                return (
                  <Pressable
                    onPress={() => navigation.navigate('CategoryProductsScreen', { categoryId: item.id })}
                    style={styles.categoryCard}
                  >
                    {imageUri ? (
                      <Image source={imageUri} style={styles.categoryImage} />
                    ) : (
                      <View style={styles.categoryPlaceholder} />
                    )}

                    <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.categoryLabel} numberOfLines={2}>
                      {item.name || 'Category'}
                    </CustomText>
                  </Pressable>
                );
              }}
            />
          </View>

          <View style={styles.section}>
            <CustomText variant="h6" fontFamily={Fonts.Bold} style={styles.sectionTitle}>
              Featured Products
            </CustomText>

            <FlatList
              data={featuredProducts}
              horizontal
              showsHorizontalScrollIndicator={false}
              keyExtractor={(item) => String(item.id)}
              contentContainerStyle={{ paddingBottom: 8 }}
              renderItem={({ item }) => (
                <ProductCard
                  product={item}
                  compact
                  onPress={() => navigation.navigate('ProductDetailsScreen', { productId: item.id })}
                />
              )}
            />
          </View>

          <View style={styles.logoutContainer}>
            <CustomText variant="h6" fontFamily={Fonts.Bold} style={styles.sectionTitle}>
              Session
            </CustomText>
            <Text style={styles.infoText}>Signed in as {firstName}</Text>
            <Text style={styles.infoText}>Use Logout to clear session and return to login.</Text>

            <View style={styles.logoutButton}>
              <Button title="Logout" onPress={logout} disabled={false} loading={false} />
            </View>
          </View>
        </>
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f6f8fb',
  },
  header: {
    marginBottom: 16,
  },
  title: {
    color: '#1e1e1e',
    marginBottom: 6,
  },
  subtitle: {
    color: '#555',
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    color: '#222',
    marginBottom: 12,
  },
  loader: {
    marginTop: 40,
  },
  categoryCard: {
    width: 120,
    height: 140,
    marginRight: 12,
    borderRadius: 16,
    backgroundColor: '#fff',
    padding: 12,
    justifyContent: 'flex-end',
    borderWidth: 1,
    borderColor: Colors.border,
  },
  categoryImage: {
    width: '100%',
    height: 78,
    resizeMode: 'cover',
    borderRadius: 12,
    marginBottom: 10,
  },
  categoryPlaceholder: {
    width: '100%',
    height: 78,
    backgroundColor: '#e8eaef',
    borderRadius: 12,
    marginBottom: 10,
  },
  categoryLabel: {
    color: '#333',
  },
  logoutContainer: {
    marginTop: 24,
    paddingHorizontal: 0,
  },
  logoutButton: {
    width: '100%',
  },
  infoText: {
    fontSize: 14,
    color: '#555',
    marginBottom: 6,
  },
});

export default ProductDashboard;
