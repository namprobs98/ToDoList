'use client';

import { motion } from 'framer-motion';
import { useDashboard } from '@/features/todo/hooks/useTodos';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  CheckCircle,
  Clock,
  AlertCircle,
  ListTodo,
  TrendingUp,
} from 'lucide-react';
import Link from 'next/link';

export default function DashboardPage() {
  const { data: dashboard, isLoading, error } = useDashboard();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen gap-4">
        <AlertCircle className="h-12 w-12 text-destructive" />
        <p className="text-muted-foreground">Failed to load dashboard data</p>
      </div>
    );
  }

  const stats = [
    {
      title: 'Total Todos',
      value: dashboard?.totalTodos || 0,
      icon: ListTodo,
      color: 'text-indigo-500',
      bgColor: 'bg-indigo-50 dark:bg-indigo-950',
    },
    {
      title: 'Completed',
      value: dashboard?.completedTodos || 0,
      icon: CheckCircle,
      color: 'text-green-500',
      bgColor: 'bg-green-50 dark:bg-green-950',
    },
    {
      title: 'In Progress',
      value: dashboard?.inProgressTodos || 0,
      icon: Clock,
      color: 'text-blue-500',
      bgColor: 'bg-blue-50 dark:bg-blue-950',
    },
    {
      title: 'Overdue',
      value: dashboard?.overdueTodos || 0,
      icon: AlertCircle,
      color: 'text-red-500',
      bgColor: 'bg-red-50 dark:bg-red-950',
    },
  ];

  const statusData = dashboard?.todosByStatus.map((s) => ({
    name: s.status.replace('_', ' '),
    value: s.count,
  })) || [];

  const priorityData = dashboard?.todosByPriority.map((p) => ({
    name: p.priority,
    value: p.count,
  })) || [];

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
      <div className="container mx-auto px-4 py-8">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-foreground">Dashboard</h1>
              <p className="text-muted-foreground mt-2">
                Your todo overview at a glance
              </p>
            </div>
            <Link
              href="/todos"
              className="px-4 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90 transition-colors"
            >
              View All Todos
            </Link>
          </div>

          {/* Stats Cards */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {stats.map((stat, index) => (
              <motion.div
                key={stat.title}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
              >
                <Card className="hover:shadow-lg transition-shadow">
                  <CardContent className="pt-6">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm font-medium text-muted-foreground">
                          {stat.title}
                        </p>
                        <p className="text-3xl font-bold mt-1">{stat.value}</p>
                      </div>
                      <div
                        className={`p-3 rounded-lg ${stat.bgColor}`}
                      >
                        <stat.icon className={`h-6 w-6 ${stat.color}`} />
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>

          {/* Charts */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Status Chart */}
            <motion.div
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.5, delay: 0.4 }}
            >
              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <TrendingUp className="h-5 w-5" />
                    Todos by Status
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {statusData.map((status, index) => (
                      <div key={status.name} className="space-y-2">
                        <div className="flex items-center justify-between text-sm">
                          <span className="font-medium capitalize">
                            {status.name}
                          </span>
                          <span className="text-muted-foreground">
                            {status.value}
                          </span>
                        </div>
                        <div className="w-full bg-muted rounded-full h-2">
                          <motion.div
                            className={`h-2 rounded-full ${
                              index === 0
                                ? 'bg-gray-500'
                                : index === 1
                                ? 'bg-blue-500'
                                : 'bg-green-500'
                            }`}
                            initial={{ width: 0 }}
                            animate={{
                              width: `${
                                dashboard
                                  ? (status.value / dashboard.totalTodos) * 100
                                  : 0
                              }%`,
                            }}
                            transition={{ duration: 0.8, delay: 0.5 + index * 0.1 }}
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </motion.div>

            {/* Priority Chart */}
            <motion.div
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.5, delay: 0.4 }}
            >
              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <TrendingUp className="h-5 w-5" />
                    Todos by Priority
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {priorityData.map((priority, index) => {
                      const colors = ['bg-green-500', 'bg-yellow-500', 'bg-red-500'];
                      return (
                        <div key={priority.name} className="space-y-2">
                          <div className="flex items-center justify-between text-sm">
                            <span className="font-medium">{priority.name}</span>
                            <span className="text-muted-foreground">
                              {priority.value}
                            </span>
                          </div>
                          <div className="w-full bg-muted rounded-full h-2">
                            <motion.div
                              className={`h-2 rounded-full ${colors[index]}`}
                              initial={{ width: 0 }}
                              animate={{
                                width: `${
                                  dashboard
                                    ? (priority.value / dashboard.totalTodos) * 100
                                    : 0
                                }%`,
                              }}
                              transition={{ duration: 0.8, delay: 0.5 + index * 0.1 }}
                            />
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </div>
        </motion.div>
      </div>
    </div>
  );
}