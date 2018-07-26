def union(a:Set[Int], b:Set[Int]) = {
  a.foreach((x) =>
    b.foreach((y) =>
      if(a(x)!=b(y))
        println(a(x))
    )
  )
}

