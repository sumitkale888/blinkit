import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, Image, SafeAreaView, ActivityIndicator } from 'react-native';
import { useAuthStore } from '@state/authStore';
import { categories as defaultCategories, imageData } from '@utils/dummyData';
import { appAxios } from '@service/apiInterceptor';
import { logout } from '@service/authService';
import Button from '@components/ui/Button';

const ProductDashboard = () => {
  const { user } = useAuthStore();
  const [categories, setCategories] = useState<any[]>(defaultCategories);
  const [products, setProducts] = useState<any[]>(
    imageData.slice(0, 8).map((src, index) => ({ id: `${index}`, image: src, name: 'Popular item' }))
  );
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      try {
        const [categoryResponse, productResponse] = await Promise.all([
          appAxios.get('/categories'),
          appAxios.get('/products'),
        ]);

        if (Array.isArray(categoryResponse.data) && categoryResponse.data.length > 0) {
          setCategories(categoryResponse.data);
        }

        if (Array.isArray(productResponse.data) && productResponse.data.length > 0) {
          const loadedProducts = productResponse.data.map((item: any, index: number) => ({
            id: item.id?.toString() || `${index}`,
            name: item.name,
            price: item.price,
            thumbnailUrl: item.thumbnailUrl,
            image: item.thumbnailUrl ? { uri: item.thumbnailUrl } : imageData[index % imageData.length],
          }));
          setProducts(loadedProducts);
        }
      } catch (error) {
        console.log('Error loading dashboard data', error);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const firstName = user?.firstName || user?.email || 'Customer';

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Welcome back{firstName ? `, ${firstName}` : ''}!</Text>
        <Text style={styles.subtitle}>Browse daily essentials</Text>
      </View>

      {loading ? (
        <ActivityIndicator size="large" color="#f8890e" style={styles.loader} />
      ) : (
        <>
          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Top Categories</Text>
            <FlatList
              data={categories}
              horizontal
              showsHorizontalScrollIndicator={false}
              keyExtractor={(item) => item.id?.toString() || item.name}
              renderItem={({ item }) => (
                <View style={styles.categoryCard}>
                  {item.imageUrl ? (
                    <Image source={{ uri: item.imageUrl }} style={styles.categoryImage} />
                  ) : item.image ? (
                    <Image source={item.image} style={styles.categoryImage} />
                  ) : (
                    <View style={styles.categoryPlaceholder} />
                  )}
                  <Text style={styles.categoryLabel} numberOfLines={2}>
                    {item.name || 'Category'}
                  </Text>
                </View>
              )}
            />
          </View>

          <View style={styles.section}>
            <Text style={styles.sectionTitle}>Featured Products</Text>
            <FlatList
              data={products}
              horizontal
              showsHorizontalScrollIndicator={false}
              keyExtractor={(item) => item.id}
              renderItem={({ item }) => (
                <View style={styles.productCard}>
                  <Image source={item.image} style={styles.productImage} />
                  <Text style={styles.productName} numberOfLines={1}>{item.name || 'Product'}</Text>
                  {item.price != null && <Text style={styles.productPrice}>₹{item.price}</Text>}
                </View>
              )}
            />
          </View>

          <View style={styles.logoutContainer}>
            <Text style={styles.sectionTitle}>Session</Text>
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
    fontSize: 24,
    fontWeight: '700',
    marginBottom: 6,
    color: '#1e1e1e',
  },
  subtitle: {
    fontSize: 16,
    color: '#555',
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '700',
    marginBottom: 12,
    color: '#222',
  },
  categoryCard: {
    width: 110,
    height: 140,
    marginRight: 12,
    borderRadius: 16,
    backgroundColor: '#fff',
    padding: 12,
    justifyContent: 'flex-end',
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 3,
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
    fontSize: 13,
    color: '#333',
  },
  productCard: {
    width: 140,
    minHeight: 180,
    marginRight: 14,
    borderRadius: 18,
    backgroundColor: '#fff',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 12,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 3,
  },
  productImage: {
    width: 100,
    height: 100,
    resizeMode: 'contain',
    marginBottom: 10,
  },
  productName: {
    fontSize: 13,
    fontWeight: '600',
    color: '#222',
    marginBottom: 4,
  },
  productPrice: {
    fontSize: 14,
    fontWeight: '700',
    color: '#f8890e',
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
  loader: {
    marginTop: 40,
  },
});

export default ProductDashboard;
