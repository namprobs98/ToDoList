'use client';

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useTodos, useCreateTodo, useDeleteTodo, useCompleteTodo } from '@/features/todo/hooks/useTodos';
import { useTodoStore } from '@/features/todo/store/todoStore';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Todo, Priority, Status, CreateTodoRequest } from '@/features/todo/types/todo';
import { formatDate, getPriorityColor, getStatusColor, cn } from '@/lib/utils';
import {
  Plus,
  Search,
  Filter,
  Trash2,
  CheckCircle,
  Calendar,
  AlertCircle,
  ChevronLeft,
  ChevronRight,
  LayoutGrid,
} from 'lucide-react';
import Link from 'next/link';

export default function TodosPage() {
  const { filters, setFilters, resetFilters } = useTodoStore();
  const { data: todosData, isLoading, error } = useTodos();
  const createTodo = useCreateTodo();
  const deleteTodo = useDeleteTodo();
  const completeTodo = useCompleteTodo();

  const [searchKeyword, setSearchKeyword] = useState('');
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [dueDateWarning, setDueDateWarning] = useState<string | null>(null);
  const [newTodo, setNewTodo] = useState<CreateTodoRequest>({
    title: '',
    description: '',
    priority: 'MEDIUM',
    dueDate: '',
  });

  const handleSearch = () => {
    setFilters({ keyword: searchKeyword, page: 0 });
  };

  const handleStatusFilter = (status: string) => {
    setFilters({ status: status === 'all' ? undefined : status as Status, page: 0 });
  };

  const handlePriorityFilter = (priority: string) => {
    setFilters({ priority: priority === 'all' ? undefined : priority as Priority, page: 0 });
  };

  const handlePageChange = (newPage: number) => {
    setFilters({ page: newPage });
  };

  const handleCreateTodo = async () => {
    if (!newTodo.title.trim()) return;

    // Check if due date is in the past
    if (newTodo.dueDate) {
      // Parse datetime-local value as local time (not UTC)
      const [datePart, timePart] = newTodo.dueDate.split('T');
      const [year, month, day] = datePart.split('-').map(Number);
      const [hours, minutes] = timePart.split(':').map(Number);

      const selectedDate = new Date(year, month - 1, day, hours, minutes);
      const now = new Date();

      if (selectedDate.getTime() <= now.getTime()) {
        setDueDateWarning('Due date đã chọn nằm trong quá khứ. Vui lòng chọn ngày khác.');
        return;
      }
    }

    setDueDateWarning(null);

    // Convert dueDate to ISO string properly
    let dueDateIso: string | undefined;
    if (newTodo.dueDate) {
      const [datePart, timePart] = newTodo.dueDate.split('T');
      const [year, month, day] = datePart.split('-').map(Number);
      const [hours, minutes] = timePart.split(':').map(Number);
      const selectedDate = new Date(year, month - 1, day, hours, minutes);
      dueDateIso = selectedDate.toISOString();
    }

    await createTodo.mutateAsync({
      ...newTodo,
      dueDate: dueDateIso,
    });
    setIsCreateOpen(false);
    setNewTodo({ title: '', description: '', priority: 'MEDIUM', dueDate: '' });
    setDueDateWarning(null);
  };

  const handleDueDateChange = (value: string) => {
    setNewTodo({ ...newTodo, dueDate: value });
    // Clear warning when user changes due date
    setDueDateWarning(null);
  };

  const handleDelete = async (id: string) => {
    if (confirm('Bạn có chắc chắn muốn xóa công việc này?')) {
      await deleteTodo.mutateAsync(id);
    }
  };

  const handleComplete = async (id: string) => {
    await completeTodo.mutateAsync(id);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
      <div className="container mx-auto px-4 py-8">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          {/* Header */}
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-foreground">Công việc</h1>
              <p className="text-muted-foreground mt-2">
                Quản lý công việc của bạn một cách hiệu quả
              </p>
            </div>
            <div className="flex items-center gap-4">
              <Link
                href="/dashboard"
                className="px-4 py-2 bg-secondary text-secondary-foreground rounded-md hover:bg-secondary/80 transition-colors"
              >
                Bảng điều khiển
              </Link>
              <Dialog open={isCreateOpen} onOpenChange={(open) => { setIsCreateOpen(open); if (!open) setDueDateWarning(null); }}>
                <DialogTrigger asChild>
                  <Button className="gap-2">
                    <Plus className="h-4 w-4" />
                    Tạo mới
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Tạo công việc mới</DialogTitle>
                  </DialogHeader>
                  <div className="space-y-4 py-4">
                    <div className="space-y-2">
                      <Label htmlFor="title">Tiêu đề *</Label>
                      <Input
                        id="title"
                        value={newTodo.title}
                        onChange={(e) => setNewTodo({ ...newTodo, title: e.target.value })}
                        placeholder="Nhập tiêu đề công việc"
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="description">Mô tả</Label>
                      <Input
                        id="description"
                        value={newTodo.description}
                        onChange={(e) => setNewTodo({ ...newTodo, description: e.target.value })}
                        placeholder="Nhập mô tả"
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="priority">Độ ưu tiên</Label>
                      <Select
                        value={newTodo.priority}
                        onValueChange={(value) => setNewTodo({ ...newTodo, priority: value as Priority })}
                      >
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="LOW">Thấp</SelectItem>
                          <SelectItem value="MEDIUM">Trung bình</SelectItem>
                          <SelectItem value="HIGH">Cao</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="dueDate">Ngày hết hạn</Label>
                      <Input
                        id="dueDate"
                        type="datetime-local"
                        value={newTodo.dueDate}
                        onChange={(e) => handleDueDateChange(e.target.value)}
                      />
                      {dueDateWarning && (
                        <motion.div
                          initial={{ opacity: 0, y: -10 }}
                          animate={{ opacity: 1, y: 0 }}
                          className="flex items-center gap-2 p-2 text-sm text-amber-600 bg-amber-50 dark:bg-amber-950 rounded-md"
                        >
                          <AlertCircle className="h-4 w-4" />
                          {dueDateWarning}
                        </motion.div>
                      )}
                    </div>
                    <Button onClick={handleCreateTodo} className="w-full">
                      Tạo công việc
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
            </div>
          </div>

          {/* Filters */}
          <Card className="mb-6">
            <CardContent className="pt-6">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex-1 relative">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Tìm kiếm công việc..."
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                    className="pl-10"
                  />
                </div>
                <Select value={filters.status || 'all'} onValueChange={handleStatusFilter}>
                  <SelectTrigger className="w-full md:w-[180px]">
                    <SelectValue placeholder="Trạng thái" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Tất cả</SelectItem>
                    <SelectItem value="TODO">Chưa làm</SelectItem>
                    <SelectItem value="IN_PROGRESS">Đang làm</SelectItem>
                    <SelectItem value="COMPLETED">Hoàn thành</SelectItem>
                  </SelectContent>
                </Select>
                <Select value={filters.priority || 'all'} onValueChange={handlePriorityFilter}>
                  <SelectTrigger className="w-full md:w-[180px]">
                    <SelectValue placeholder="Độ ưu tiên" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Tất cả</SelectItem>
                    <SelectItem value="LOW">Thấp</SelectItem>
                    <SelectItem value="MEDIUM">Trung bình</SelectItem>
                    <SelectItem value="HIGH">Cao</SelectItem>
                  </SelectContent>
                </Select>
                <Button variant="outline" onClick={resetFilters}>
                  Đặt lại
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Todo List */}
          {isLoading ? (
            <div className="flex items-center justify-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
          ) : error ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-12 gap-4">
                <AlertCircle className="h-12 w-12 text-destructive" />
                <p className="text-muted-foreground">Không thể tải danh sách công việc</p>
              </CardContent>
            </Card>
          ) : todosData?.content.length === 0 ? (
            <Card>
              <CardContent className="flex flex-col items-center justify-center py-12 gap-4">
                <LayoutGrid className="h-12 w-12 text-muted-foreground" />
                <p className="text-muted-foreground">Chưa có công việc nào</p>
                <Button onClick={() => setIsCreateOpen(true)}>Tạo công việc đầu tiên</Button>
              </CardContent>
            </Card>
          ) : (
            <>
              <div className="grid gap-4">
                <AnimatePresence>
                  {todosData?.content.map((todo: Todo, index: number) => (
                    <motion.div
                      key={todo.id}
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      exit={{ opacity: 0, y: -20 }}
                      transition={{ duration: 0.3, delay: index * 0.05 }}
                    >
                      <Card className="hover:shadow-lg transition-shadow">
                        <CardContent className="pt-6">
                          <div className="flex items-start justify-between gap-4">
                            <div className="flex-1 min-w-0">
                              <div className="flex items-center gap-2 mb-2">
                                <span
                                  className={cn(
                                    'px-2 py-1 rounded-full text-xs font-medium',
                                    getPriorityColor(todo.priority)
                                  )}
                                >
                                  {todo.priority === 'LOW' ? 'Thấp' : todo.priority === 'MEDIUM' ? 'Trung bình' : 'Cao'}
                                </span>
                                <span
                                  className={cn(
                                    'px-2 py-1 rounded-full text-xs font-medium',
                                    getStatusColor(todo.status)
                                  )}
                                >
                                  {todo.status === 'TODO' ? 'Chưa làm' : todo.status === 'IN_PROGRESS' ? 'Đang làm' : 'Hoàn thành'}
                                </span>
                              </div>
                              <h3 className="text-lg font-semibold truncate">{todo.title}</h3>
                              {todo.description && (
                                <p className="text-sm text-muted-foreground mt-1 line-clamp-2">
                                  {todo.description}
                                </p>
                              )}
                              {todo.dueDate && (
                                <div className="flex items-center gap-1 mt-2 text-sm text-muted-foreground">
                                  <Calendar className="h-4 w-4" />
                                  <span>Hạn: {formatDate(todo.dueDate)}</span>
                                </div>
                              )}
                            </div>
                            <div className="flex items-center gap-2">
                              {todo.status !== 'COMPLETED' && (
                                <Button
                                  variant="outline"
                                  size="icon"
                                  onClick={() => handleComplete(todo.id)}
                                  title="Đánh dấu hoàn thành"
                                >
                                  <CheckCircle className="h-4 w-4 text-green-500" />
                                </Button>
                              )}
                              <Button
                                variant="outline"
                                size="icon"
                                onClick={() => handleDelete(todo.id)}
                                title="Xóa công việc"
                              >
                                <Trash2 className="h-4 w-4 text-red-500" />
                              </Button>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    </motion.div>
                  ))}
                </AnimatePresence>
              </div>

              {/* Pagination */}
              {todosData && todosData.totalPages > 1 && (
                <div className="flex items-center justify-center gap-2 mt-6">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => handlePageChange(filters.page! - 1)}
                    disabled={todosData.first}
                  >
                    <ChevronLeft className="h-4 w-4" />
                  </Button>
                  <span className="text-sm text-muted-foreground">
                    Trang {todosData.page + 1} / {todosData.totalPages}
                  </span>
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => handlePageChange(filters.page! + 1)}
                    disabled={todosData.last}
                  >
                    <ChevronRight className="h-4 w-4" />
                  </Button>
                </div>
              )}
            </>
          )}
        </motion.div>
      </div>
    </div>
  );
}