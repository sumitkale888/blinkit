import React, { useEffect, useState } from 'react';
import { FlatList, Image, Pressable, SafeAreaView, StyleSheet, View } from 'react-native';
import { appAxios } from '@service/apiInterceptor';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from '@components/ui/CustomText';
import ContentState from '@components/ui/ContentState';
import { useNavigation } from '@react-navigation/native';

type CategoryDto = {
  id: number | string;
  name?: string;
  slug?: string;
  description?: string;
  active?: boolean;
  imageUrl?: string;
  image?: any;
};

const CategoriesScreen = () => {
  const navigation = useNavigation<any>();

  const [categories, setCategories] = useState<CategoryDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let mounted = true;

    const load = async () => {
      setLoading(true);
      setError(null);

      try {
        const res = await appAxios.get('/categories');
        const data = Array.isArray(res.data) ? res.data : res.data?.data ?? [];
        if (mounted) {
          setCategories(data);
        }
      } catch (e: any) {
        if (mounted) {
          setError(e?.response?.data?.message || e?.message || 'Failed to load categories');
        }
      } finally {
        if (mounted) {
          setLoading(false);
        }
      }
    };

    load();
    return () => {
      mounted = false;
    };
  }, []);

  const renderItem = ({ item }: { item: CategoryDto }) => {
    const imageUri: { uri: string } | undefined =
      typeof item?.imageUrl === 'string'
        ? { uri: item.imageUrl }
        : item?.image?.uri
          ? item.image
          : undefined;

    return (
      <Pressable
        style={styles.card}
        onPress={() =>
          navigation.navigate('CategoryProductsScreen', {
            categoryId: item.id,
          })
        }
      >
        <View style={styles.imageWrap}>
          {imageUri ? (
            <Image source={imageUri} style={styles.image} resizeMode="cover" />
          ) : (
            <View style={styles.imagePlaceholder} />
          )}
        </View>

        <CustomText variant="body" fontFamily={Fonts.Medium} style={styles.label} numberOfLines={2}>
          {item.name || 'Category'}
        </CustomText>
      </Pressable>
    );
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <ContentState status="loading" text="Loading categories..." />
      </SafeAreaView>
    );
  }

  if (error) {
    return (
      <SafeAreaView style={styles.container}>
        <ContentState status="error" text={error} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <CustomText variant="h5" fontFamily={Fonts.Bold} style={styles.title}>
          Categories
        </CustomText>
      </View>

      {categories.length === 0 ? (
        <ContentState status="empty" text="No categories found" />
      ) : (
        <FlatList
          data={categories}
          keyExtractor={(item) => String(item.id)}
          renderItem={renderItem}
          numColumns={2}
          contentContainerStyle={styles.listContent}
          columnWrapperStyle={styles.columnWrapper}
        />
      )}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f6f8fb' },
  header: { paddingHorizontal: 16, paddingTop: 14, paddingBottom: 8 },
  title: { color: Colors.text },
  listContent: { paddingHorizontal: 12, paddingBottom: 24 },
  columnWrapper: { gap: 12, justifyContent: 'space-between' },
  card: {
    width: '48%',
    backgroundColor: 'white',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: Colors.border,
    padding: 10,
    marginBottom: 12,
  },
  imageWrap: {
    width: '100%',
    height: 110,
    borderRadius: 12,
    backgroundColor: '#f5f6fb',
    overflow: 'hidden',
    marginBottom: 10,
  },
  image: { width: '100%', height: '100%' },
  imagePlaceholder: { flex: 1, backgroundColor: '#f0f2f6' },
  label: { color: Colors.text },
});

export default CategoriesScreen;
