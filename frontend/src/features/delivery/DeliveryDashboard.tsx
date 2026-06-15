import React, { FC, useEffect, useState } from 'react';
import { ActivityIndicator, FlatList, SafeAreaView, StyleSheet, Text, View } from 'react-native';
import CustomSafeAreaView from '@components/global/CustomSafeAreaView';
import Button from '@components/ui/Button';
import { fetchAdminUsers, logout } from '@service/authService';
import { useAuthStore } from '@state/authStore';

const DeliveryDashboard: FC = () => {
  const { user } = useAuthStore();
  const [adminUsers, setAdminUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadAdminUsers = async () => {
      setLoading(true);
      setError(null);
      try {
        const users = await fetchAdminUsers();
        setAdminUsers(Array.isArray(users) ? users : []);
      } catch (err: any) {
        console.log('Failed to load admin users', err);
        setError('Unable to load admin data.');
      } finally {
        setLoading(false);
      }
    };

    loadAdminUsers();
  }, []);

  return (
    <CustomSafeAreaView>
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.title}>Delivery / Admin Dashboard</Text>
          <Text style={styles.subtitle}>Welcome back{user?.firstName ? `, ${user.firstName}` : ''}</Text>
        </View>

        {loading ? (
          <ActivityIndicator size="large" color="#f8890e" style={styles.loader} />
        ) : (
          <>
            <Text style={styles.sectionTitle}>Registered Users</Text>
            {error ? (
              <Text style={styles.errorText}>{error}</Text>
            ) : (
              <FlatList
                data={adminUsers}
                keyExtractor={(item) => item.id?.toString() || item.email || Math.random().toString()}
                renderItem={({ item }) => (
                  <View style={styles.userCard}>
                    <Text style={styles.userName}>{item.firstName || item.email || 'Unknown'}</Text>
                    <Text style={styles.userMeta}>{item.email}</Text>
                    <Text style={styles.userMeta}>{item.role}</Text>
                  </View>
                )}
              />
            )}
          </>
        )}

        <View style={styles.logoutButton}>
          <Button title="Logout" onPress={logout} disabled={false} loading={false} />
        </View>
      </SafeAreaView>
    </CustomSafeAreaView>
  );
};

export default DeliveryDashboard;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f6f8fb',
    padding: 16,
  },
  header: {
    marginBottom: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    marginBottom: 8,
    color: '#1e1e1e',
  },
  subtitle: {
    fontSize: 16,
    color: '#555',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '700',
    marginBottom: 12,
    color: '#222',
  },
  userCard: {
    backgroundColor: '#fff',
    borderRadius: 14,
    padding: 16,
    marginBottom: 10,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
    elevation: 3,
  },
  userName: {
    fontSize: 16,
    fontWeight: '700',
    color: '#222',
    marginBottom: 6,
  },
  userMeta: {
    fontSize: 14,
    color: '#555',
  },
  loader: {
    marginTop: 40,
  },
  errorText: {
    color: '#d9534f',
    marginBottom: 12,
  },
  logoutButton: {
    marginTop: 16,
  },
});